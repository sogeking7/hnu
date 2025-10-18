import { Component, inject, OnInit } from '@angular/core';
import { IonCard, IonCardContent, IonCardHeader, IonCardTitle, IonContent, IonHeader } from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';
import { Router } from '@angular/router';
import { ChatModel } from '@hnu-app/services/types/chat-model';
import { ChatService } from '@hnu-app/services/chat.service';

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
  chats: ChatModel[] = [];

  private readonly chatService = inject(ChatService);
  private readonly router = inject(Router);

  constructor() {
  }

  ngOnInit() {
    this.loadChats();
  }

  async loadChats() {
    try {
      const res = await this.chatService.getUserChats();
      this.chats = res;
    } finally {

    }
  }

  navigateChat(chatId: string) {
    this.router.navigate(['/chat', chatId]);
  }
}
