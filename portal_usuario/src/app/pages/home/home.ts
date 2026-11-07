import { Component, ElementRef, inject, OnInit, signal, ViewChild } from '@angular/core';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { CameraService } from '../../services/camera-service';

@Component({
  selector: 'app-home',
  imports: [ReactiveFormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  @ViewChild('video') videoRef!: ElementRef<HTMLVideoElement>;
  @ViewChild('canvas') canvasRef!: ElementRef<HTMLCanvasElement>;
  readonly camera = inject(CameraService);
  issueForm = new FormGroup({
    issueDescription: new FormControl(''),
  });

  ngOnInit(): void {}

  async openCamera() {
    this.camera.start().then(() => {
      this.camera.setVideoRef(this.videoRef.nativeElement);
    });
  }

  startGpsAccess() {}

  async takePicture() {
    this.camera.takePicture(this.videoRef.nativeElement, this.canvasRef.nativeElement);
  }

  closeCamera() {
    this.camera.stop();
  }

  toggleCamera() {
    if (this.camera.hasAccess()) {
      this.closeCamera();
    } else {
      this.openCamera();
    }
  }
}
