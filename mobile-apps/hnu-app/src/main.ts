import {
  enableProdMode,
  ErrorHandler,
  inject,
  Injectable,
  isDevMode,
  LOCALE_ID,
  provideAppInitializer
} from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, Router, RouteReuseStrategy } from '@angular/router';
import { IonicRouteStrategy, provideIonicAngular } from '@ionic/angular/standalone';

import { routes } from '@hnu-app/app.routes';
import { AppComponent } from '@hnu-app/app.component';
import { environment } from '@hnu-env/environment';
import { provideTransloco, Translation, TranslocoLoader, TranslocoService } from '@jsverse/transloco';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpInterceptorFn,
  provideHttpClient,
  withInterceptors
} from '@angular/common/http';
import { provideHttpCache, withHttpCacheInterceptor } from '@ngneat/cashew';
import { catchError, from, switchMap, throwError, zip } from 'rxjs';
import { AuthService } from '@hnu-app/services/auth.service';
import { BASE_PATH } from '@hnu-app/bm-api';
import { StorageService } from '@hnu-app/services/storage.service';
import { AppService } from '@hnu-app/services/app.service';
import { App } from '@capacitor/app';
import { ToastService } from '@hnu-app/services/toast.service';
import { provideServiceWorker } from '@angular/service-worker';
import { Capacitor } from '@capacitor/core';
import ru from '@angular/common/locales/ru';
// import kk from '@angular/common/locales/kk';
import { registerLocaleData } from '@angular/common';
import * as SentryAngular from '@sentry/angular';

registerLocaleData(ru);

// registerLocaleData(kk);

if (environment.production) {
  enableProdMode();
}

let appVersion = '';

const customHttpInterceptorFn: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const tr = inject(TranslocoService);
  const auth = inject(AuthService);
  const storage = inject(StorageService);
  const nt = inject(ToastService);
  const tokenPromise = storage.getToken();
  return zip(from(tokenPromise), getAppVersion()).pipe(
    switchMap(([token, appVersion]) => {
      let headers = new HttpHeaders({
        'Accept-Language': tr.getActiveLang(),
        'Accept': 'application/json',
        'x-client-app-version': appVersion,
      });
      if (token) {
        headers = headers.set('Authorization', 'Bearer ' + token);
      }
      return next(req.clone({ headers: headers }));
    }),
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401) {
        auth.setUser(undefined);
        router.navigate(['/login']);
      } else if (err.status === 470) {
        router.navigate(['/update']);
      } else if ([570].includes(err.status)) {
        let error = err.error;
        if (req.responseType === 'arraybuffer') {
          error = JSON.parse(new TextDecoder().decode(err.error));
        }
        const errorId = error.id;
        setTimeout(() => {
          const r: any = err;
          if (!r.handled) {
            if (error.description) {
              nt.error(`${tr.translate('error.error')} [${errorId}]`, error.description);
            } else {
              nt.error(`${tr.translate('error.error')} [${errorId}]`, JSON.stringify(error));
            }
          }
        });
      }
      return throwError(() => err);
    })
  );
};

async function getAppVersion(): Promise<string> {
  if (appVersion) {
    return appVersion;
  }
  if (AppService.isMobile()) {
    try {
      appVersion = await App.getInfo().then(info => info.version);
      return appVersion;
    } catch (e) {
      console.error('err', e);
    }
  } else if (AppService.isElectron()) {
    appVersion = localStorage.getItem('version') ?? '0.0.0';
  }
  return appVersion ?? '0.0.0';
}

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpCache({ ttl: 10000 }),
    provideHttpClient(withInterceptors([withHttpCacheInterceptor(), customHttpInterceptorFn])),
    provideTransloco({
      config: {
        availableLangs: ['ru', 'kk'],
        defaultLang: 'ru',
        // Remove this option if your application
        // doesn't support changing language in runtime.
        fallbackLang: 'ru',
        reRenderOnLangChange: true,
        prodMode: environment.production,
      },
      loader: TranslocoHttpLoader
    }),
    {
      provide: LOCALE_ID,
      useValue: 'ru-KZ'
    },
    {
      provide: RouteReuseStrategy,
      useClass: IonicRouteStrategy,
    },
    {
      provide: ErrorHandler,
      useValue: SentryAngular.createErrorHandler(),
    },
    provideAppInitializer(async () => {
      const auth = inject(AuthService);
      const tr = inject(TranslocoService);
      const storageService = inject(StorageService);

      const cachedLang = await storageService.getLang();
      const defaultLang = tr.config.defaultLang;

      const activeLang = cachedLang ?? defaultLang;

      tr.setActiveLang(activeLang);

      return auth.getMyUserInfo()
        .then((resp) => {
          auth.setUser(resp);
        }, (err: HttpErrorResponse) => {
          console.error('Error response', err);
          if (err.status === 401 || err.error?.message === 'notAuthenticated') {

          }
        });
    }),
    {
      provide: BASE_PATH,
      useValue: environment.apiUrl,
    },
    provideIonicAngular({
      mode: 'ios',
      innerHTMLTemplatesEnabled: true,
    }),
    provideRouter(routes),
    provideServiceWorker('ngsw-worker.js', {
      enabled: !isDevMode() && !Capacitor.isNativePlatform(),
      registrationStrategy: 'registerWhenStable:30000'
    }),
  ],
});
