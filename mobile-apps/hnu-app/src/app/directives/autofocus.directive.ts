import {
  Directive,
  ElementRef,
  AfterViewInit,
  Input,
  OnDestroy,
  inject
} from '@angular/core';

/**
 * Automatically focuses an element after view initialization.
 * Handles Ionic components, accessibility, and edge cases.
 *
 * @example
 * <ion-input appAutofocus></ion-input>
 * <input appAutofocus [autofocusDelay]="200">
 */
@Directive({
  selector: '[appAutofocus]',
  standalone: true // Modern Angular best practice
})
export class AutofocusDirective implements AfterViewInit, OnDestroy {
  private readonly el = inject(ElementRef);

  /**
   * Delay before focusing (ms). Useful for animations or slow devices.
   * Default: 300
   */
  @Input() autofocusDelay = 300;

  private focusTimeout?: number;

  ngAfterViewInit(): void {
    this.focusTimeout = window.setTimeout(() => {
      this.setFocus();
    }, this.autofocusDelay);
  }

  private setFocus(): void {
    try {
      const element = this.el.nativeElement;

      if (!element) {
        console.warn('[AutofocusDirective] Element reference is null');
        return;
      }

      if (typeof element.setFocus === 'function') {
        element.setFocus();
        return;
      }

      if (typeof element.focus === 'function') {
        element.focus();

        // Scroll into view
        element.scrollIntoView({ behavior: 'smooth', block: 'center' });
      } else {
        console.warn(
          '[AutofocusDirective] Element is not focusable:',
          element.tagName
        );
      }
    } catch (error) {
      console.error('[AutofocusDirective] Failed to focus element:', error);
    }
  }

  ngOnDestroy(): void {
    // Clean up timeout to prevent memory leaks
    if (this.focusTimeout) {
      clearTimeout(this.focusTimeout);
    }
  }
}
