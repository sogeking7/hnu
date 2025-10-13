import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('@hnu-app/components/tabs/tabs.module').then(m => m.TabsPageModule)
  }
];
