import { Component, inject, OnInit } from '@angular/core';
import { TranscriptionService } from '@hnu-app/services/transcription.service';
import { IonButton, IonIcon } from '@ionic/angular/standalone';

@Component({
  selector: 'app-microphone-modal',
  templateUrl: './microphone.modal.html',
  styleUrls: ['./microphone.modal.scss'],
  imports: [
    IonButton,
    IonIcon,
  ]
})
export class MicrophoneModal implements OnInit {

  isTranscribing = false;

  private readonly transService = inject(TranscriptionService);

  constructor() {
  }

  ngOnInit() {
    return;
  }


  fileToBlob = (file: File): Blob => file;

  async onAudioSelected(ev: Event) {
    const input = ev.target as HTMLInputElement;
    const file = input?.files?.[0];
    if (!file) {
      return;
    }

    input.value = '';

    try {
      this.isTranscribing = true;

      const {
        enhanced_text,
      } = await this.transService.getTextFromAudio(this.fileToBlob(file));

    } finally {
      this.isTranscribing = false;
    }
  }
}
