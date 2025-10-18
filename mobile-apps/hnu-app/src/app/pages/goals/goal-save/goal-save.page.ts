import { Component, inject, OnInit } from '@angular/core';
import { IonButton, IonCol, IonContent, IonHeader, IonIcon, IonInput, IonLabel, IonRow } from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { GoalService } from '@hnu-app/services/goal.service';
import { ToastService } from '@hnu-app/services/toast.service';
import { HxLoadingDirective } from '@hnu-app/directives/loading/loading.directive';

@Component({
  selector: 'app-goal-save-page',
  templateUrl: './goal-save.page.html',
  styleUrls: ['./goal-save.page.scss'],
  imports: [
    IonContent,
    IonHeader,
    IonIcon,
    WrapperComponent,
    IonCol,
    IonLabel,
    IonRow,
    ReactiveFormsModule,
    IonInput,
    IonButton,
    HxLoadingDirective
  ]
})
export class GoalSavePage implements OnInit {
  form = new FormGroup({
    name: new FormControl<string | null>(null, [Validators.required]),
    durationMonth: new FormControl<number | null>(null, [Validators.required]),
    monthlyInvest: new FormControl<number | null>(null, [Validators.required]),
    target: new FormControl<number | null>(null, [Validators.required]),
  });
  isLoading = {
    save: false,
  };

  private readonly goalService = inject(GoalService);
  private readonly aRoute = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly nt = inject(ToastService);

  constructor() {
  }

  ngOnInit() {
    return;
  }

  close() {
    this.router.navigate(['/main/goals']);
  }

  async save() {
    console.log('f');
    Object.entries(this.form.controls).forEach(([name, control]) => {
      control.markAsDirty();
      control.updateValueAndValidity();
    });

    if (this.form.valid) {
      const formModel = this.form.value;
      console.log(formModel);
      if (formModel.monthlyInvest == null || formModel.monthlyInvest == undefined
        || formModel.durationMonth == null || formModel.durationMonth == undefined
        || formModel.name
        || formModel.target == null || formModel.target == undefined) {
        throw new Error('required.fields');
      }
      this.isLoading.save = true;
      try {
        const estimatedDate = '';
        await this.goalService.saveGoal({
          name: formModel.name!,
          durationMonth: formModel.durationMonth!,
          monthlyInvest: formModel.monthlyInvest!,
          target: formModel.target!,
          createDate: new Date().toISOString(),
          estimatedDate: estimatedDate
        });
        this.clearForm();
        this.form.reset();
        this.form.markAsUntouched();
        this.form.markAsPristine();
        await this.nt.saved();
        await this.router.navigateByUrl('/main/goals');
      } finally {
        this.isLoading.save = false;
      }
    } else {
      this.form.markAllAsTouched();
    }
  }

  clearForm() {
    this.form.patchValue({
      target: null,
      name: null,
      durationMonth: null,
      monthlyInvest: null

    });
  }
}
