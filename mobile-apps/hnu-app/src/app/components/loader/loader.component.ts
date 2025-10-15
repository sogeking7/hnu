import { Component, input } from '@angular/core';

@Component({
  imports: [],
  selector: 'app-loader',
  template: `
  <div class="loader__wrapper"
    [class.is--inline]="inline()"
    [class.is--background]="background()">
    <div class="loader"></div>
    <div class="loader__backdrop"></div>
  </div>
  `,
  styleUrls: ['loader.component.scss']
})
export class LoaderComponent {
  inline = input(false);
  background = input(false);
}
