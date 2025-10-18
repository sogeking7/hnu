export * from './chat.service';
import { ChatService } from './chat.service';
export * from './system.service';
import { SystemService } from './system.service';
export const APIS = [ChatService, SystemService];
