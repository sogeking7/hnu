import { Injectable } from '@angular/core';
import { Preferences } from '@capacitor/preferences';
import { Pin } from '@hnu-app/pages/login/login.page';
import { Authenticator } from '@hnu-app/nu-api';

interface OtpInfo {
  date: number;
  authenticator: Authenticator;
}

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  readonly pin_key = 'pins';

  async savePin(phone: string, token: string, pinCode: string) {
    try {
      const data = await this.get(this.pin_key);
      const list = (JSON.parse(data ?? '[]') as Pin[])
        .filter(r => r.phone !== phone);

      list.push({code: pinCode, phone: phone, token: token});
      await this.set(this.pin_key, JSON.stringify(list));
    } catch (ex) {
      console.error(ex);
    }
  }

  async getPin(pinCode: string) {
    let data = await this.get(this.pin_key);
    try {
      let list = JSON.parse(data ?? '[]') as Pin[];
      return list.find(r => r.code === pinCode);
    } catch (ex) {
      console.error(ex);
      return undefined;
    }
  }

  getPhone() {
    return this.get('phone');
  }

  savePhone(phone: string) {
    return this.set('phone', phone);
  }

  removePhone() {
    return this.remove('phone');
  }

  getToken() {
    return this.get('token');
  }

  saveToken(token: string) {
    return this.set('token', token);
  }

  private get(name: string) {
    return Preferences.get({
      key: name
    }).then(res => res.value ?? undefined);
  }

  private set(name: string, value: string) {
    return Preferences.set({key: name, value: value});
  }

  private remove(name: string) {
    return Preferences.remove({key: name});
  }

  async saveOtpSendDate(info: OtpInfo) {
    return this.set('otp', JSON.stringify(info));
  }

  getOtpSendInfo(): Promise<OtpInfo | undefined> {
    return this.get('otp').then(json => {
      if (json) {
        return JSON.parse(json);
      }
      return undefined;
    });
  }
}
