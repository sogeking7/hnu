import { Component, inject, OnInit } from '@angular/core';
import {
  IonCard,
  IonCardContent,
  IonCardHeader,
  IonCardTitle,
  IonContent,
  IonFab,
  IonFabButton,
  IonHeader,
  IonIcon
} from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';
import { GoalService } from '@hnu-app/services/goal.service';
import { GoalModel } from '@hnu-app/nu-api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-goals-list-page',
  templateUrl: 'goal-list.page.html',
  styleUrls: ['goal-list.page.scss'],
  imports: [
    IonContent,
    WrapperComponent,
    IonHeader,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardContent,
    IonFab,
    IonFabButton,
    IonIcon,
  ]
})
export class GoalListPage implements OnInit {
  goals: GoalModel[] = [];

  private readonly goalService = inject(GoalService);
  private readonly router = inject(Router);


  constructor() {

  }

  ngOnInit() {
    this.loadGoals();
  }

  async loadGoals() {
    try {
      const res = await this.goalService.getGoals();
      this.goals = res;
    } finally {

    }
  }

  navigateGoal(id: string) {
    this.router.navigate(['/main/goals', id]);
  }

  createGoal() {
    this.router.navigate(['/goals', 'new']);
  }
}
