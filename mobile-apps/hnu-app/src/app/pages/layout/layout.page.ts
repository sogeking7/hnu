import { CommonModule } from '@angular/common';
import { Component, inject, isDevMode } from '@angular/core';
import { Router } from '@angular/router';
import { IonApp, IonIcon, IonLabel, IonRouterOutlet, IonTabBar, IonTabButton, IonTabs, } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  addCircleOutline,
  carOutline,
  cartOutline,
  closeOutline,
  ellipsisHorizontal,
  ellipsisVertical,
  listOutline,
  personOutline,
  removeCircleOutline,
  todayOutline,
  trashOutline
} from 'ionicons/icons';
import { AuthService } from '@hnu-app/services/auth.service';
import { LoaderComponent } from '@hnu-app/components/loader/loader.component';

@Component({
  imports: [
    CommonModule, IonApp, IonRouterOutlet,
    LoaderComponent, IonTabs, IonTabButton, IonTabBar, IonIcon, IonLabel,
  ],
  selector: 'app-layout',
  templateUrl: './layout.page.html',
  styleUrls: ['./layout.page.scss']
})
export class LayoutPage {
  isLoader = {
    lock: false,
  };
  isDevMode = isDevMode();

  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);

  constructor() {
    addIcons({
      cartOutline,
      todayOutline,
      listOutline,
      carOutline,
      ellipsisHorizontal,
      ellipsisVertical,
      personOutline,
      trashOutline,
      removeCircleOutline,
      addCircleOutline,
      closeOutline,
    });
  }

  async lock() {
    this.isLoader.lock = true;
    try {
      await this.auth.logout();
      this.router.navigateByUrl('/login');
    } catch (error) {
      console.log('err', error);
    } finally {
      this.isLoader.lock = false;
    }
  }
}
