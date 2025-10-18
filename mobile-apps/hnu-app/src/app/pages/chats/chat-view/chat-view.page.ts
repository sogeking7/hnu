import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ConversationHistoryResponse, MessageSchema } from '@hnu-app/ml';
import { ChatService } from '@hnu-app/services/chat.service';
import { AuthUserModel } from '@hnu-app/nu-api';
import { AuthService } from '@hnu-app/services/auth.service';

@Component({
  selector: 'app-chats-view-page',
  templateUrl: 'chat-view.page.html',
  styleUrls: ['chat-view.page.scss'],
  imports: [IonContent, WrapperComponent, CommonModule, FormsModule, IonIcon],
})
export class ChatViewPage implements OnInit {
  @ViewChild(IonContent) content?: IonContent;

  user?: AuthUserModel;
  data?: ConversationHistoryResponse;
  messages: MessageSchema[] = [];

  isLoading = {
    send: false,
    user: false,
  };
  draft = '';

  private readonly aRoute = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly chatService = inject(ChatService);
  private readonly auth = inject(AuthService);

  constructor() {
  }

  async ngOnInit() {
    await this.loadUser();
    this.data = this.aRoute.snapshot.data['chat'] ?? undefined;
    this.messages = this.data ? this.data.messages : [];
  }

  async loadUser() {
    this.isLoading.user = true;
    try {
      const res = await this.auth.getMyUserInfo();
      this.user = res;
    } finally {
      this.isLoading.user = false;
    }
  }

  async send() {
    console.log(this.user);
    console.log(this.data);

    if (!this.data || !this.user) {
      return;
    }
    const text = this.draft.trim();
    if (!text) {
      return;
    }

    try {
      this.isLoading.send = true;
      this.messages.push({
        role: 'user',
        content: this.draft
      });
      this.scrollToBottom();
      this.draft = '';
      const res = await this.chatService.sendMessage({
        conversation_id: this.data.conversation_id,
        user_message: text,
        user_id: this.user.id,
        user_name: this.user.lastname + ' ' + this.user.firstname + ' ' + this.user.patronymic,
      });
      this.messages.push({
        role: 'assistant',
        content: res.assistant_message
      });
      this.scrollToBottom();

    } finally {
      this.isLoading.send = false;
    }
  }

  private scrollToBottom(): void {
    // slight delay so DOM paints first
    setTimeout(() => this.content?.scrollToBottom(200), 0);
  }

  private now(): string {
    const d = new Date();
    return d.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
  }

  close() {
    this.router.navigate(['/main/chat']);
  }
}
