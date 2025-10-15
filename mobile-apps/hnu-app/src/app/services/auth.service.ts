import { inject, Injectable, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { AuthResourceService, AuthUserModel, ConfirmOtpRequest, SendOtpRequest } from '@hnu-app/nu-api';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  user?: AuthUserModel;

  auth$ = signal<AuthUserModel | undefined>(undefined);

  private readonly auth = inject(AuthResourceService);

  constructor() {

  }

  sendOtpCode(req: SendOtpRequest) {
    return firstValueFrom(this.auth.sendOtpCode(req));
  }

  confirmOtpCode(req: ConfirmOtpRequest) {
    return firstValueFrom(this.auth.confirmOtpCode(req));
  }

  getMyUserInfo(): Promise<AuthUserModel> {
    return firstValueFrom(this.auth.getMyUserInfo());
  }

  logout() {
    this.setUser(undefined);
    return firstValueFrom(this.auth.logout());
  }

  unlockSessionById(sessionId: string) {
    return firstValueFrom(this.auth.unlockSessionById(sessionId));
  }

  setUser(session?: AuthUserModel) {
    this.user = session;
    this.auth$.set(session);
  }
}
