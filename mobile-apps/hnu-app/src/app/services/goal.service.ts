import { inject, Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { GoalModel, GoalResourceService, GoalSaveRequest, SaveResult } from '@hnu-app/nu-api';

@Injectable({
  providedIn: 'root'
})
export class GoalService {
  private readonly goal = inject(GoalResourceService);

  getUserGoals(userId: string): Promise<GoalModel[]> {
    return firstValueFrom(this.goal.getUserGoals(userId));
  }

  getGoals(): Promise<GoalModel[]> {
    return firstValueFrom(this.goal.getGoals());
  }

  removeGoal(goalId: string): Promise<void> {
    return firstValueFrom(this.goal.removeGoal(goalId));
  }

  saveGoal(req: GoalSaveRequest): Promise<SaveResult> {
    return firstValueFrom(this.goal.saveGoal(req));
  }

  getGoalById(goalId: string): Promise<GoalModel> {
    return firstValueFrom(this.goal.getGoalById(goalId));
  }
}
