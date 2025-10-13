import { inject, Injectable } from '@angular/core';
import { ToastController } from '@ionic/angular/standalone';

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  duration = 5000;

  private readonly nt = inject(ToastController);

  async show(opts: {
    title?: string;
    content?: string;
    data?: any;
    position?: 'top' | 'middle' | 'bottom';
    duration?: number;
    color?: string;
  }) {
    return this.nt.create({
      header: opts.title ? opts.title : undefined,
      message: opts.content ? opts.content : undefined,
      position: opts.position,
      duration: opts.duration ?? this.duration,
      color: opts.color,
    }).then(t => t.present());
  }

  async success(title: string, content: string, data?: any) {
    await this.nt.create({
      header: title,
      message: content,
      color: 'success',
      duration: this.duration,
    }).then(t => t.present());
  }

  async error(title: string, content: string, data?: any) {
    await this.nt.create({
      header: title,
      message: content,
      color: 'danger',
      duration: this.duration,
    }).then(t => t.present());
  }

  async info(title: string, content: string, data?: any) {
    await this.nt.create({
      header: title,
      message: content,
      color: 'primary',
      duration: this.duration,
    }).then(t => t.present());
  }

  async warning(title: string, content: string, data?: any) {
    await this.nt.create({
      header: title,
      message: content,
      color: 'warning',
      duration: this.duration,
    }).then(t => t.present());
  }

  async saved(title = '') {
    await this.success(title, 'nt.dataSaved');
  }

  async removed(title = '') {
    await this.success(title, 'nt.dataRemoved');
  }
}
