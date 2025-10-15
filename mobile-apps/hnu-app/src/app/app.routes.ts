import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'main/profile',
    pathMatch: 'full',
  },
  {
    path: 'main',
    loadComponent: () => import('@hnu-app/pages/layout/layout.page').then(m => m.LayoutPage),
    children: [
      {
        path: 'profile',
        loadComponent: () => import('@hnu-app/pages/profile/profile.page').then(m => m.ProfilePage)
      },
      {
        path: 'tab2',
        loadComponent: () => import('@hnu-app/pages/tab2/tab2.page').then(m => m.Tab2Page)
      },
      {
        path: 'tab3',
        loadComponent: () => import('@hnu-app/pages/tab3/tab3.page').then(m => m.Tab3Page)
      },
    ],
  },
  {
    path: 'login',
    loadComponent: () => import('@hnu-app/pages/login/login.page').then(m => m.LoginPage),
  },
];
