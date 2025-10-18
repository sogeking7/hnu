import { Component, inject, OnInit } from '@angular/core';
import { IonContent, IonHeader, IonIcon } from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';
import { Router } from '@angular/router';
import { routes } from '@hnu-app/app.routes';

@Component({
  selector: 'app-home-page',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
  imports: [
    IonContent,
    IonHeader,
    IonIcon,
    WrapperComponent,
  ]
})
export class HomePage implements OnInit {
  private readonly router = inject(Router);

  constructor() {
  }

  ngOnInit() {
  }

  navigateUser() {
    this.router.navigate(['/main/profile']);
  }
}
