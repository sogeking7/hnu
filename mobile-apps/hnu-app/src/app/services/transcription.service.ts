import { inject, Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { TranscriptionResponse, TranscriptionService as TranscriptionResourceService } from '@hnu-app/ml'

@Injectable({
  providedIn: 'root'
})
export class TranscriptionService {
  private readonly trans = inject(TranscriptionResourceService);

  getTextFromAudio(file: Blob): Promise<TranscriptionResponse> {
    return firstValueFrom(this.trans.transcribeAudioTranscribePost(file));
  }
}
