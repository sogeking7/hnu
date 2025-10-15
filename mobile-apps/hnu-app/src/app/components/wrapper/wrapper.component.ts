import { Component, input } from '@angular/core';
import { LoaderComponent } from '../loader/loader.component';

@Component({
  imports: [
    LoaderComponent,
  ],
  selector: 'app-wrapper',
  templateUrl: './wrapper.component.html',
  styleUrls: ['./wrapper.component.scss'],
})
export class WrapperComponent {
  loading = input(false);
  classes = input('');
}
