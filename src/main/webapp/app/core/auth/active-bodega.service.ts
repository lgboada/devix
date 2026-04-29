import { Injectable, Signal, signal } from '@angular/core';

import { ActiveBodega } from './active-bodega.model';
import { StateStorageService } from './state-storage.service';

@Injectable({ providedIn: 'root' })
export class ActiveBodegaService {
  private readonly activeBodega = signal<ActiveBodega | null>(null);
  private readonly availableBodegas = signal<ActiveBodega[]>([]);

  constructor(private readonly stateStorageService: StateStorageService) {}

  trackActiveBodega(): Signal<ActiveBodega | null> {
    return this.activeBodega.asReadonly();
  }

  trackAvailableBodegas(): Signal<ActiveBodega[]> {
    return this.availableBodegas.asReadonly();
  }

  initializeBodegas(bodegas: ActiveBodega[]): void {
    this.availableBodegas.set(bodegas);
    const storedId = this.stateStorageService.getActiveBodegaId();
    const fromSession = storedId != null ? bodegas.find(b => b.bodegaId === storedId) : undefined;
    const active = fromSession ?? bodegas.find(b => b.principal) ?? bodegas[0] ?? null;
    this.activeBodega.set(active);
    if (active) {
      this.stateStorageService.storeActiveBodegaId(active.bodegaId);
    } else {
      this.stateStorageService.clearActiveBodegaId();
    }
  }

  selectBodega(bodegaId: number): void {
    const selected = this.availableBodegas().find(b => b.bodegaId === bodegaId);
    if (!selected) return;
    this.activeBodega.set(selected);
    this.stateStorageService.storeActiveBodegaId(selected.bodegaId);
  }

  clear(): void {
    this.activeBodega.set(null);
    this.availableBodegas.set([]);
    this.stateStorageService.clearActiveBodegaId();
  }
}
