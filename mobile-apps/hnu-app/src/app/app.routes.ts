import { ResolveFn, Routes } from '@angular/router';
import { GoalModel } from '@hnu-app/nu-api';
import { inject } from '@angular/core';
import { GoalService } from '@hnu-app/services/goal.service';
import { ChatService } from '@hnu-app/services/chat.service';
import { ConversationHistoryResponse } from '@hnu-app/ml';

const goalResolver: ResolveFn<GoalModel> = route => {
  let id = route.paramMap.get('id');
  if (!id) {
    return Promise.reject('Goal id is undefined');
  }
  return inject(GoalService).getGoalById(id);
};

const chatResolver: ResolveFn<ConversationHistoryResponse> = route => {
  let id = route.paramMap.get('id');
  if (!id) {
    return Promise.reject('Chat id is undefined');
  }
  return inject(ChatService).getChatById(id);
};

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'main/dashboard',
    pathMatch: 'full',
  },
  {
    path: 'main',
    loadComponent: () => import('@hnu-app/pages/layout/layout.page').then(m => m.LayoutPage),
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('@hnu-app/pages/home/home.page').then(m => m.HomePage)
      },
      {
        path: 'profile',
        loadComponent: () => import('@hnu-app/pages/profile/profile.page').then(m => m.ProfilePage)
      },
      {
        path: 'goals',
        loadComponent: () => import('@hnu-app/pages/goals/goal-list/goal-list.page').then(m => m.GoalListPage)
      },
      {
        path: 'goals/:id',
        loadComponent: () => import('@hnu-app/pages/goals/goal-view/goal-view.page').then(m => m.GoalViewPage),
        resolve: {
          goal: goalResolver,
        },
      },
      {
        path: 'chat',
        loadComponent: () => import('@hnu-app/pages/chats/chat-list/chat-list.page').then(m => m.ChatListPage)
      },
    ],
  },
  {
    path: 'goals/new',
    loadComponent: () => import('@hnu-app/pages/goals/goal-save/goal-save.page').then(m => m.GoalSavePage)
  },
  {
    path: 'chat/:id',
    loadComponent: () => import('@hnu-app/pages/chats/chat-view/chat-view.page').then(m => m.ChatViewPage),
    resolve: {
      chat: chatResolver,
    },
  },
  {
    path: 'login',
    loadComponent: () => import('@hnu-app/pages/login/login.page').then(m => m.LoginPage),
  },
];
