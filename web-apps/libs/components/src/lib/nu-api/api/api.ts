export * from './auth-resource.service';
import { AuthResourceService } from './auth-resource.service';
export * from './greeting-resource.service';
import { GreetingResourceService } from './greeting-resource.service';
export * from './telegram-resource.service';
import { TelegramResourceService } from './telegram-resource.service';
export const APIS = [AuthResourceService, GreetingResourceService, TelegramResourceService];
