import { Injectable, Signal, signal } from '@angular/core';

import { ActiveCentro } from './active-centro.model';
import { StateStorageService } from './state-storage.service';

@Injectable({ providedIn: 'root' })
export class ActiveCentroService {
  private readonly activeCentro = signal<ActiveCentro | null>(null);
  private readonly availableCentros = signal<ActiveCentro[]>([]);

  constructor(private readonly stateStorageService: StateStorageService) {}

  trackActiveCentro(): Signal<ActiveCentro | null> {
    return this.activeCentro.asReadonly();
  }

  trackAvailableCentros(): Signal<ActiveCentro[]> {
    return this.availableCentros.asReadonly();
  }

  initializeCentros(centros: ActiveCentro[]): void {
    this.availableCentros.set(centros);
    const storedId = this.stateStorageService.getActiveCentroId();
    const fromSession = storedId != null ? centros.find(c => c.centroId === storedId) : undefined;
    const active = fromSession ?? centros.find(c => c.principal) ?? centros[0] ?? null;
    this.activeCentro.set(active);
    if (active) {
      this.stateStorageService.storeActiveCentroId(active.centroId);
    } else {
      this.stateStorageService.clearActiveCentroId();
    }
  }

  selectCentro(centroId: number): void {
    const selected = this.availableCentros().find(c => c.centroId === centroId);
    if (!selected) return;
    this.activeCentro.set(selected);
    this.stateStorageService.storeActiveCentroId(selected.centroId);
  }

  clear(): void {
    this.activeCentro.set(null);
    this.availableCentros.set([]);
    this.stateStorageService.clearActiveCentroId();
  }
}
