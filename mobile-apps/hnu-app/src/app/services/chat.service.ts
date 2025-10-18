import { Injectable } from '@angular/core';
import { ChatModel, mockChats } from '@hnu-app/services/types/chat-model';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  // private readonly transaction = inject(TransactionResourceService);

  getUserChats(): Promise<ChatModel[]> {
    console.log(mockChats);
    return Promise.resolve(mockChats);
  }

  getChatById(chatId: string): Promise<ChatModel> {
    const chat = mockChats.find(c => c.id === chatId)!;
    return Promise.resolve(chat);
  }

}
