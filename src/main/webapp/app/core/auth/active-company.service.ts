import { Injectable, Signal, signal } from '@angular/core';

import { ActiveCompany } from 'app/core/auth/active-company.model';
import { StateStorageService } from 'app/core/auth/state-storage.service';

@Injectable({ providedIn: 'root' })
export class ActiveCompanyService {
  private readonly activeCompany = signal<ActiveCompany | null>(null);
  private readonly availableCompanies = signal<ActiveCompany[]>([]);

  constructor(private readonly stateStorageService: StateStorageService) {}

  trackActiveCompany(): Signal<ActiveCompany | null> {
    return this.activeCompany.asReadonly();
  }

  trackAvailableCompanies(): Signal<ActiveCompany[]> {
    return this.availableCompanies.asReadonly();
  }

  initializeCompanies(companies: ActiveCompany[]): void {
    this.availableCompanies.set(companies);
    const storedNoCia = this.stateStorageService.getActiveCompanyNoCia();
    const fromSession = storedNoCia != null ? companies.find(company => company.noCia === storedNoCia) : undefined;
    const active = fromSession ?? companies.find(company => company.principal) ?? companies[0] ?? null;
    this.activeCompany.set(active);
    if (active) {
      this.stateStorageService.storeActiveCompanyNoCia(active.noCia);
      return;
    }
    this.stateStorageService.clearActiveCompanyNoCia();
  }

  selectCompany(noCia: number): void {
    const selected = this.availableCompanies().find(company => company.noCia === noCia);
    if (!selected) {
      return;
    }
    this.activeCompany.set(selected);
    this.stateStorageService.storeActiveCompanyNoCia(selected.noCia);
  }

  clear(): void {
    this.activeCompany.set(null);
    this.availableCompanies.set([]);
    this.stateStorageService.clearActiveCompanyNoCia();
  }
}
