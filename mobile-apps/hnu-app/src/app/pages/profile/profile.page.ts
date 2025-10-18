import { Component, inject, OnInit } from '@angular/core';
import { IonButton, IonContent, IonGrid, IonHeader, IonIcon, IonTitle, IonToolbar } from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';
import { HxLoadingDirective } from '@hnu-app/directives/loading/loading.directive';
import { AuthService } from '@hnu-app/services/auth.service';
import { Router } from '@angular/router';
import { AuthUserModel } from '@hnu-app/nu-api';
import { PhonePipe } from '@hnu-app/pipes/phone.pipe';
import { Location } from '@angular/common';

@Component({
  selector: 'app-profile-page',
  templateUrl: 'profile.page.html',
  styleUrls: ['profile.page.scss'],
  imports: [
    IonContent,
    WrapperComponent,
    IonGrid,
    IonButton,
    HxLoadingDirective,
    PhonePipe,
    IonToolbar,
    IonTitle,
    IonHeader,
    IonIcon
  ]
})
export class ProfilePage implements OnInit {
  user?: AuthUserModel;
  isLoading = {
    user: false,
    logout: false,
  };

  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly location = inject(Location);

  async ngOnInit() {
    await this.loadUser();
  }

  async loadUser() {
    this.isLoading.user = true;
    try {
      this.user = await this.auth.getMyUserInfo();
    } finally {
      this.isLoading.user = false;
    }
  }

  async logout() {
    try {
      this.isLoading.logout = true;
      await this.auth.logout();
      await this.router.navigateByUrl('/login');
    } finally {
      this.isLoading.logout = false;
    }
  }

  close() {
    this.location.back();
  }
}
