import { Injectable } from '@angular/core';
import { BehaviorSubject, distinctUntilChanged, map, shareReplay } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {
  private readonly requestsSubject = new BehaviorSubject<number>(0);
  private readonly activeRequests$ = this.requestsSubject.asObservable();

  readonly isLoading$ = this.activeRequests$.pipe(
    map(count => count > 0),
    distinctUntilChanged(),
    shareReplay(1)
  );

  showLoader(): void {
    this.requestsSubject.next(this.requestsSubject.value + 1);
  }

  hideLoader(): void {
    const current = this.requestsSubject.value;
    this.requestsSubject.next(Math.max(0, current - 1));
  }
}

