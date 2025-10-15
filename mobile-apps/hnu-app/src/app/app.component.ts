import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { IonRouterOutlet, } from '@ionic/angular/standalone';
import { AuthService } from '@hnu-app/services/auth.service';
import { LoaderService } from './services/loader.service';
import { LoaderComponent } from '@hnu-app/components/loader/loader.component';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  imports: [CommonModule, IonRouterOutlet, LoaderComponent]
})
export class AppComponent {
  private readonly loaderService = inject(LoaderService);

  isLoading$ = this.loaderService.isLoading$;
}
