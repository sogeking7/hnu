import { Directive, effect, ElementRef, inject, input, Renderer2 } from '@angular/core';

@Directive({
  standalone: true,
  selector: '[hxLoading]'
})
export class HxLoadingDirective {

  hxLoading = input.required();

  private readonly LOADING_CLASS = 'hxLoading';

  private el = inject(ElementRef);
  private renderer = inject(Renderer2);

  constructor() {
    effect(() => {
      const loading = this.hxLoading();
      const button = this.el.nativeElement;

      if (loading) {
        this.renderer.setAttribute(button, 'disabled', 'true');
        this.renderer.addClass(button, this.LOADING_CLASS);
      } else {
        this.renderer.removeAttribute(button, 'disabled');
        this.renderer.removeClass(button, this.LOADING_CLASS);
      }
    });
  }
}
