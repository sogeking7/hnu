import { Component } from '@angular/core';
import { IonContent } from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';

@Component({
  selector: 'app-tab3-page',
  templateUrl: 'tab3.page.html',
  styleUrls: ['tab3.page.scss'],
  imports: [
    IonContent,
    WrapperComponent
  ]
})
export class Tab3Page {

  constructor() {
  }

}
