export * from './chat.service';
import { ChatService } from './chat.service';
export * from './system.service';
import { SystemService } from './system.service';
export * from './transcription.service';
import { TranscriptionService } from './transcription.service';
export const APIS = [ChatService, SystemService, TranscriptionService];
