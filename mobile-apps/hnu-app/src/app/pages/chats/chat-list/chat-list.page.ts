import { Component, inject, OnInit } from '@angular/core';
import { IonCard, IonCardContent, IonCardHeader, IonCardTitle, IonContent, IonHeader } from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';
import { Router } from '@angular/router';
import { ChatModel } from '@hnu-app/services/types/chat-model';
import { ChatService } from '@hnu-app/services/chat.service';
import { AuthService } from '@hnu-app/services/auth.service';
import { ConversationDetail } from '@hnu-app/ml';

@Component({
  selector: 'app-chats-list-page',
  templateUrl: './chat-list.page.html',
  styleUrls: ['./chat-list.page.scss'],
  imports: [
    IonCard,
    IonCardContent,
    IonCardHeader,
    IonCardTitle,
    IonContent,
    IonHeader,
    WrapperComponent
  ]
})
export class ChatListPage implements OnInit {
  chats: ConversationDetail[] = [];
  userId?: string;
  isLoading = {
    chats: false,
    user: false,
  };


  private readonly auth = inject(AuthService);
  private readonly chatService = inject(ChatService);
  private readonly router = inject(Router);

  constructor() {
  }

  async ngOnInit() {
    await this.loadUser();
    await this.loadChats();
  }

  async loadUser() {
    this.isLoading.user = true;
    try {
      const res = await this.auth.getMyUserInfo();
      this.userId = res.id;
    } finally {
      this.isLoading.user = false;
    }
  }

  async loadChats() {
    if (!this.userId) {
      return;
    }
    try {
      const res = await this.chatService.getUserChats(this.userId);
      this.chats = res;
    } finally {

    }
  }

  navigateChat(chatId: string) {
    this.router.navigate(['/chat', chatId]);
  }
}
