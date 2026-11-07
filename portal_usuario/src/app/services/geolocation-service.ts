import { computed, Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class GeolocationService {
  private readonly _coords = signal<number[] | null>(null);
  readonly hasCoords = computed(() => Array.isArray(this._coords()));

  private readonly options = {
    enableHighAccuracy: true,
    maximumAge: 30000,
    timeout: 27000,
  };

  watchGeolocation() {
    navigator.geolocation.watchPosition(
      (position) => {
        console.log('Deu certo geolocation');
        this._coords.set([position.coords.latitude, position.coords.longitude]);
      },
      (error) => {
        console.log('Deu errado a geolocation', error.message);
      },
      this.options,
    );
  }
}
