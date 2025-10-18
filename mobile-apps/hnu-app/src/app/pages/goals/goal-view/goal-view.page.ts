import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GoalModel } from '@hnu-app/nu-api';
import { IonContent, IonHeader, IonIcon } from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';

@Component({
  selector: 'app-goal-view',
  templateUrl: './goal-view.page.html',
  styleUrls: ['./goal-view.page.scss'],
  imports: [
    IonContent,
    IonHeader,
    WrapperComponent,
    IonIcon
  ]
})
export class GoalViewPage implements OnInit {
  data?: GoalModel;

  private readonly aRoute = inject(ActivatedRoute);
  private readonly router = inject(Router);

  constructor() {
  }

  ngOnInit() {
    this.data = this.aRoute.snapshot.data['goal'] ?? undefined;
  }

  close() {
    this.router.navigate(['/main/goals']);
  }
}
