import { inject, Injectable } from '@angular/core';
import {
  ChatRequest,
  ChatResponse,
  ChatService as ChatResourceService,
  ConversationDetail,
  ConversationHistoryResponse,
  CreateConversationRequest,
  CreateConversationResponse
} from '@hnu-app/ml';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly chat = inject(ChatResourceService);

  getUserChats(userId: string): Promise<ConversationDetail[]> {
    return firstValueFrom(this.chat.getUserConversationsUsersUserIdConversationsGet(userId));
  }

  getChatById(chatId: string): Promise<ConversationHistoryResponse> {
    return firstValueFrom(this.chat.getConversationMessagesConversationsConversationIdMessagesGet(chatId));
  }

  sendMessage(req: ChatRequest): Promise<ChatResponse> {
    return firstValueFrom(this.chat.chatChatPost(req));
  }

  createChat(req: CreateConversationRequest): Promise<CreateConversationResponse> {
    return firstValueFrom(this.chat.createConversationConversationsPost(req));
  }

}
