import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { WrapperComponent } from '@hnu-app/components/wrapper/wrapper.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatModel } from '@hnu-app/services/types/chat-model';
import { ActivatedRoute, Router } from '@angular/router';

type ChatMessage = {
  id: string;
  role: 'me' | 'bot';
  text: string;
  time: string;
};

@Component({
  selector: 'app-chats-view-page',
  templateUrl: 'chat-view.page.html',
  styleUrls: ['chat-view.page.scss'],
  imports: [IonContent, WrapperComponent, CommonModule, FormsModule, IonIcon],
})
export class ChatViewPage implements OnInit {
  @ViewChild(IonContent) content?: IonContent;

  data?: ChatModel;
  messages: ChatMessage[] = [
    {id: crypto.randomUUID(), role: 'bot', text: 'Hi! How can I help?', time: this.now()},
  ];

  draft = '';

  private readonly aRoute = inject(ActivatedRoute);
  private readonly router = inject(Router);

  constructor() {
  }

  ngOnInit() {
    this.data = this.aRoute.snapshot.data['chat'] ?? undefined;
  }

  send(): void {
    const text = this.draft.trim();
    if (!text) return;

    // Push my message
    this.messages.push({
      id: crypto.randomUUID(),
      role: 'me',
      text,
      time: this.now(),
    });
    this.draft = '';
    this.scrollToBottom();

    // Fake bot reply (for demo)
    setTimeout(() => {
      this.messages.push({
        id: crypto.randomUUID(),
        role: 'bot',
        text: 'Got it 👍',
        time: this.now(),
      });
      this.scrollToBottom();
    }, 500);
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
