import { Component, inject, viewChild } from '@angular/core';
import {
  IonButton,
  IonCol,
  IonContent,
  IonIcon,
  IonInput,
  IonLabel,
  IonRow,
} from '@ionic/angular/standalone';
import { FormsModule } from '@angular/forms';
import { NgTemplateOutlet } from '@angular/common';
import { AuthService } from '@hnu-app/services/auth.service';
import { Authenticator } from '@hnu-app/nu-api';
import { StorageService } from '@hnu-app/services/storage.service';
import { addIcons } from 'ionicons';
import { checkmarkCircleOutline } from 'ionicons/icons';
import { Router } from '@angular/router';
import { MaskitoDirective } from '@maskito/angular';
import { MaskitoElementPredicate, MaskitoOptions, maskitoTransform } from '@maskito/core';
import { differenceInSeconds } from 'date-fns';

export interface Pin {
  code: string;
  phone: string;
  token: string;
}

interface PinValue {
  value?: number;
  active: boolean;
  invalid: boolean;
}

@Component({
  selector: 'app-login-page',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  imports: [
    IonContent,
    FormsModule,
    IonButton,
    IonRow,
    IonCol,
    IonIcon,
    IonInput,
    IonLabel,
    MaskitoDirective,
    NgTemplateOutlet,
  ]
})
export class LoginPage {
  inputPhone = viewChild<IonInput>('inputPhone');
  inputCode = viewChild<IonInput>('inputCode');

  readonly TIMEOUT = 120;
  code?: string;
  invalidCode = false;
  maskedPhone?: string;
  hiddenPhone?: string;
  isLoading = {
    confirmCode: false,
    sendCode: false,
    saveStore: false,
  };
  view: 'pin' | 'phone' | 'code' | 'set-pin' | 'selector' = 'pin';
  time = this.TIMEOUT;

  protected readonly Authenticator = Authenticator;
  readonly phoneMask: MaskitoOptions = {mask: [/\d/, /\d/, /\d/, ' ', /\d/, /\d/, /\d/, ' ', /\d/, /\d/, ' ', /\d/, /\d/]};

  readonly phoneMaskPredicate: MaskitoElementPredicate = async (el: any) => (el as HTMLIonInputElement).getInputElement();
  readonly codeMask: MaskitoOptions = {mask: [/\d/, ' ', /\d/, ' ', /\d/, ' ', /\d/, ' ', /\d/]};

  readonly codeMaskPredicate: MaskitoElementPredicate = async (el: any) => (el as HTMLIonInputElement).getInputElement();
  pinValues: PinValue[] = [
    {active: false, invalid: false},
    {active: false, invalid: false},
    {active: false, invalid: false},
    {active: false, invalid: false},
  ];

  validation = {
    errorPhone: false,
  };

  private phone?: string;
  private countDownTimeout?: NodeJS.Timeout;

  private readonly auth = inject(AuthService);
  private readonly storage = inject(StorageService);
  private readonly router = inject(Router);

  constructor() {
    addIcons({checkmarkCircleOutline});
  }

  async ionViewDidEnter() {
    const phone = await this.storage.getPhone();
    if (!phone) {
      return;
    }
    this.hiddenPhone = this.hidePhone(phone);
    this.phone = phone;
    this.view = 'code';
    this.storage.getOtpSendInfo().then(info => {
      if (info) {
        const {date} = info;
        const timePassed = differenceInSeconds(Date.now(), Number(date));
        this.time = this.TIMEOUT - timePassed;
        if (this.time > 0) {
          this.startCountDown();
        } else {
          this.time = 0;
        }
      }
    });

    setTimeout(async () => {
      const input = this.inputCode();
      if (input) {
        const el = await input.getInputElement();
        el.focus();
      }
    });
  }

  async sendCode() {
    if (!this.phone) {
      return;
    }
    if (this.phone.length !== 11) {
      this.validation.errorPhone = true;
      return;
    }
    this.validation.errorPhone = false;
    this.isLoading.sendCode = true;
    try {
      await this.auth.sendOtpCode({
        phone: this.phone,
        authenticator: Authenticator.TELEGRAM,
      });
      await this.storage.saveOtpSendDate({date: Date.now(), authenticator: Authenticator.TELEGRAM});
      await this.storage.savePhone(this.phone);
      this.view = 'code';
      this.hiddenPhone = this.hidePhone(this.phone);
      setTimeout(async () => {
        this.inputCode()?.setFocus();
        this.startCountDown();
      });
    } finally {
      this.isLoading.sendCode = false;
    }
  }

  async changePhone() {
    await this.storage.removePhone();
    this.code = undefined;
    this.phone = undefined;
    this.maskedPhone = undefined;
    this.view = 'phone';

    this.pinValues.forEach(r => {
      r.active = false;
      r.value = undefined;
      r.invalid = false;
    });
    setTimeout(async () => {
      const input = this.inputPhone();
      if (input) {
        console.log('[login] set focus on input');
        await input.setFocus();
      }
    });
  }

  async onChangePhone() {
    if (this.maskedPhone) {
      this.phone = `${this.formatPhone(this.maskedPhone)}`;
      if (this.phone.length === 10) {
        this.phone = `7${this.phone}`;
      }
    } else {
      this.phone = undefined;
    }
  }


  async confirmCode() {
    if (!this.code || !this.phone) {
      return;
    }
    this.isLoading.confirmCode = true;
    try {
      const phone = this.phone;
      const code = this.formatCode(this.code);
      const {token} = await this.auth.confirmOtpCode({phone: phone, code: code});
      await this.storage.saveToken(token);
      await this.storage.removePhone();
      const user = await this.auth.getMyUserInfo();
      this.auth.setUser(user);
      this.view = 'set-pin';
    } catch (ex) {
      this.invalidCode = true;
      this.code = undefined;
    } finally {
      this.isLoading.confirmCode = false;
    }
  }

  async onCodeChange() {
    if (this.code?.length === 9) {
      await this.confirmCode();
    }
  }

  async setPinValue(value: number) {
    const pinValue = this.pinValues.find(r => !r.active);
    if (pinValue) {
      pinValue.active = true;
      pinValue.value = value;
      if (pinValue.invalid) {
        this.pinValues.forEach(pin => pin.invalid = false);
      }
    }
    const countPinValue = this.pinValues.filter(r => r.active).length;
    if (countPinValue === 4) {
      const pinCode = this.pinValues.map(r => r.value).join('');
      try {
        if (this.view === 'pin') {
          const foundPin = await this.storage.getPin(pinCode);
          if (foundPin) {
            await this.storage.saveToken(foundPin.token);
            await this.auth.unlockSessionById(foundPin.token);
            const user = await this.auth.getMyUserInfo();
            this.auth.setUser(user);
            this.reset();
            await this.router.navigateByUrl('');
          } else {
            this.pinValues = [
              {active: false, invalid: true},
              {active: false, invalid: true},
              {active: false, invalid: true},
              {active: false, invalid: true},
            ];
          }
        } else if (this.view === 'set-pin') {
          const user = await this.auth.getMyUserInfo();
          this.auth.setUser(user);
          console.log('user', JSON.stringify(user));
          const token = await this.storage.getToken();
          if (token) {
            await this.storage.savePin(user.phone, token, pinCode);
            await Promise.all([]);
            this.reset();
            await this.router.navigateByUrl('');
          } else {
            this.reset();
            await this.router.navigateByUrl('');
          }
        }
      } catch (e) {
        this.reset();
        await this.router.navigateByUrl('');
      }
    }
  }

  removeLastPinValue() {
    this.pinValues.reverse();
    const value = this.pinValues.find(r => r.active);
    if (value) {
      value.active = false;
      value.value = undefined;
    }
    this.pinValues.reverse();
  }

  async resendCode() {
    const otpInfo = await this.storage.getOtpSendInfo();
    if (otpInfo) {
      this.time = this.TIMEOUT;
      await this.sendCode();
    }
  }

  private formatPhone(phone: string) {
    return maskitoTransform(phone, {mask: [/\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/]});
  }

  private reset() {
    this.code = undefined;
    this.phone = undefined;
    this.hiddenPhone = undefined;
    this.view = 'pin';
    this.invalidCode = false;

    this.pinValues.forEach(r => {
      r.active = false;
      r.value = undefined;
      r.invalid = false;
    });
  }

  private formatCode(code: string) {
    return maskitoTransform(code, {mask: [/\d/, /\d/, /\d/, /\d/, /\d/]});
  }

  private hidePhone(phone: string) {
    return `+7 *** *** ${phone.slice(-4, -2)} ${phone.slice(-2)}`;
  }

  private startCountDown() {
    clearInterval(this.countDownTimeout);
    this.countDownTimeout = setInterval(() => {
      this.time -= 1;
      if (this.time === 0) {
        clearInterval(this.countDownTimeout);
      }
    }, 1000);
  }
}
