import { computed, ElementRef, Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CameraService {
  private readonly _stream = signal<MediaStream | null>(null);
  private readonly _error = signal('');
  private readonly _picture = signal<string | null>(null);
  private readonly _files = signal<File[] | []>([]);

  readonly stream = this._stream.asReadonly();
  readonly error = this._error.asReadonly();
  readonly hasAccess = computed(() => this._stream() !== null);
  readonly filesNames = computed(() => this._files().map((file) => file.name));

  async start(): Promise<void> {
    if (this._stream()) return;
    this._error.set('');

    if (!navigator.mediaDevices?.getUserMedia) {
      this._error.set('Navegador sem suporte à câmera ou página fora de HTTPS.');
      return;
    }

    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        video: { facingMode: 'environment', width: { ideal: 1280 }, height: { ideal: 720 } },
        audio: false,
      });
      this._stream.set(stream);
    } catch (e) {}
  }

  async setVideoRef(video: HTMLVideoElement) {
    video.srcObject = this._stream();
    await video.play();
  }

  takePicture(video: HTMLVideoElement, canvas: HTMLCanvasElement) {
    // usa a resolução real do stream, não o tamanho exibido na tela
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    canvas.getContext('2d')!.drawImage(video, 0, 0, canvas.width, canvas.height);

    // Base64 para exibir na tela
    this._picture.set(canvas.toDataURL('image/jpeg', 0.9));

    // Blob/File para enviar ao backend
    canvas.toBlob(
      (blob) => {
        if (!blob) return;
        const file = new File([blob], `foto-${Date.now()}.jpg`, { type: 'image/jpeg' });
        console.log('Arquivo criado com sucesso!', file.name);
        this._files.update((files) => [...files, file]);
      },
      'image/jpeg',
      0.9,
    );
  }

  removeFile(fileName: string) {
    console.log('Filename: ', fileName);
    this._files.update((files) => files.filter((file) => file.name != fileName));
  }

  stop(): void {
    this._stream()
      ?.getTracks()
      .forEach((t) => t.stop());
    this._stream.set(null);
  }
}
