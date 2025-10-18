import {
  enableProdMode,
  inject,
  isDevMode,
  provideAppInitializer
} from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, Router, RouteReuseStrategy } from '@angular/router';
import { IonicRouteStrategy, provideIonicAngular } from '@ionic/angular/standalone';
import { routes } from '@hnu-app/app.routes';
import { AppComponent } from '@hnu-app/app.component';
import { environment } from '@hnu-env/environment';
import {
  HttpErrorResponse,
  HttpHeaders,
  HttpInterceptorFn,
  provideHttpClient,
  withInterceptors
} from '@angular/common/http';
import { provideHttpCache, withHttpCacheInterceptor } from '@ngneat/cashew';
import { catchError, from, switchMap, throwError, zip } from 'rxjs';
import { AuthService } from '@hnu-app/services/auth.service';
import { BASE_PATH } from '@hnu-app/nu-api';
import { StorageService } from '@hnu-app/services/storage.service';
import { ToastService } from '@hnu-app/services/toast.service';
import { provideServiceWorker } from '@angular/service-worker';
import { Capacitor } from '@capacitor/core';

if (environment.production) {
  enableProdMode();
}

const customHttpInterceptorFn: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const auth = inject(AuthService);
  const storage = inject(StorageService);
  const nt = inject(ToastService);
  const tokenPromise = storage.getToken();
  return zip(from(tokenPromise)).pipe(
    switchMap(([token]) => {
      let headers = new HttpHeaders({
        'Accept': 'application/json',
        'x-client-app-version': '1.0.0'
      });
      if (token) {
        headers = headers.set('Authorization', 'Bearer ' + token);
      }
      return next(req.clone({headers: headers}));
    }),
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401) {
        auth.setUser(undefined);
        router.navigate(['/login']);
      } else if (err.status === 470) {
        router.navigate(['/update']);
      } else if (err.status) {
        let error = err.error;
        if (req.responseType === 'arraybuffer') {
          error = JSON.parse(new TextDecoder().decode(err.error));
        }
        const errorId = error.id;
        setTimeout(async () => {
          const r: any = err;
          if (!r.handled) {
            if (error.description) {
              await nt.error(`${errorId}`, error.description);
            } else {
              await nt.error(`${errorId}`, JSON.stringify(error));
            }
          }
        });
      }
      return throwError(() => err);
    })
  );
};

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpCache({ttl: 10000}),
    provideHttpClient(withInterceptors([withHttpCacheInterceptor(), customHttpInterceptorFn])),
    {
      provide: RouteReuseStrategy,
      useClass: IonicRouteStrategy,
    },
    provideAppInitializer(async () => {
      const auth = inject(AuthService);
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
    }), provideServiceWorker('ngsw-worker.js', {
      enabled: !isDevMode(),
      registrationStrategy: 'registerWhenStable:30000'
    }),
  ],
});
