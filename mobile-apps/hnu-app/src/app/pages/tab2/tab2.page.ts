import { Component } from '@angular/core';
import { IonContent } from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';

@Component({
  selector: 'app-tab2-page',
  templateUrl: 'tab2.page.html',
  styleUrls: ['tab2.page.scss'],
  imports: [
    IonContent,
    WrapperComponent,
  ]
})
export class Tab2Page {

  constructor() {
  }

}
