import { Injectable, Signal, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';
import { Observable, ReplaySubject, of } from 'rxjs';
import { catchError, map, shareReplay, switchMap, tap } from 'rxjs/operators';

import { ActiveCompany } from 'app/core/auth/active-company.model';
import { ActiveCentro } from 'app/core/auth/active-centro.model';
import { ActiveBodega } from 'app/core/auth/active-bodega.model';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { ActiveCentroService } from 'app/core/auth/active-centro.service';
import { ActiveBodegaService } from 'app/core/auth/active-bodega.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { Account } from 'app/core/auth/account.model';
import { ApplicationConfigService } from '../config/application-config.service';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private readonly userIdentity = signal<Account | null>(null);
  private readonly authenticationState = new ReplaySubject<Account | null>(1);
  private accountCache$?: Observable<Account> | null;

  private readonly translateService = inject(TranslateService);
  private readonly http = inject(HttpClient);
  private readonly stateStorageService = inject(StateStorageService);
  private readonly activeCompanyService = inject(ActiveCompanyService);
  private readonly activeCentroService = inject(ActiveCentroService);
  private readonly activeBodegaService = inject(ActiveBodegaService);
  private readonly router = inject(Router);
  private readonly applicationConfigService = inject(ApplicationConfigService);

  authenticate(identity: Account | null): void {
    this.userIdentity.set(identity);
    this.authenticationState.next(this.userIdentity());
    if (!identity) {
      this.accountCache$ = null;
      this.activeCompanyService.clear();
      this.activeCentroService.clear();
      this.activeBodegaService.clear();
    }
  }

  trackCurrentAccount(): Signal<Account | null> {
    return this.userIdentity.asReadonly();
  }

  hasAnyAuthority(authorities: string[] | string): boolean {
    const userIdentity = this.userIdentity();
    if (!userIdentity) {
      return false;
    }
    if (!Array.isArray(authorities)) {
      authorities = [authorities];
    }
    return userIdentity.authorities.some((authority: string) => authorities.includes(authority));
  }

  identity(force?: boolean): Observable<Account | null> {
    if (!this.accountCache$ || force) {
      this.accountCache$ = this.fetch().pipe(
        switchMap((account: Account) =>
          this.fetchAccountCompanies().pipe(
            tap(companies => this.activeCompanyService.initializeCompanies(companies)),
            switchMap(() => {
              const noCia = this.activeCompanyService.trackActiveCompany()()?.noCia;
              return noCia != null ? this.fetchAccountCentros(noCia) : of([]);
            }),
            tap(centros => this.activeCentroService.initializeCentros(centros)),
            switchMap(() => {
              const centroId = this.activeCentroService.trackActiveCentro()()?.centroId;
              return centroId != null ? this.fetchAccountBodegas(centroId) : of([]);
            }),
            tap(bodegas => this.activeBodegaService.initializeBodegas(bodegas)),
            map(() => account),
          ),
        ),
        tap((account: Account) => {
          this.authenticate(account);
          if (!this.stateStorageService.getLocale()) {
            this.translateService.use(account.langKey);
          }
          this.navigateToStoredUrl();
        }),
        shareReplay(),
      );
    }
    return this.accountCache$.pipe(catchError(() => of(null)));
  }

  /** Cambia la compañía activa y recarga centros + bodegas en cascada. */
  selectCompanyAndReload(noCia: number): Observable<void> {
    this.activeCompanyService.selectCompany(noCia);
    this.activeCentroService.clear();
    this.activeBodegaService.clear();
    return this.fetchAccountCentros(noCia).pipe(
      tap(centros => this.activeCentroService.initializeCentros(centros)),
      switchMap(() => {
        const centroId = this.activeCentroService.trackActiveCentro()()?.centroId;
        return centroId != null ? this.fetchAccountBodegas(centroId) : of([]);
      }),
      tap(bodegas => this.activeBodegaService.initializeBodegas(bodegas)),
      map(() => undefined),
    );
  }

  /** Cambia el centro activo y recarga bodegas. */
  selectCentroAndReload(centroId: number): Observable<void> {
    this.activeCentroService.selectCentro(centroId);
    this.activeBodegaService.clear();
    return this.fetchAccountBodegas(centroId).pipe(
      tap(bodegas => this.activeBodegaService.initializeBodegas(bodegas)),
      map(() => undefined),
    );
  }

  /** Cambia la bodega activa (sin recarga de dependientes). */
  selectBodega(bodegaId: number): void {
    this.activeBodegaService.selectBodega(bodegaId);
  }

  isAuthenticated(): boolean {
    return this.userIdentity() !== null;
  }

  getAuthenticationState(): Observable<Account | null> {
    return this.authenticationState.asObservable();
  }

  private fetch(): Observable<Account> {
    return this.http.get<Account>(this.applicationConfigService.getEndpointFor('api/account'));
  }

  private fetchAccountCompanies(): Observable<ActiveCompany[]> {
    return this.http
      .get<ActiveCompany[]>(this.applicationConfigService.getEndpointFor('api/account/companies'))
      .pipe(catchError(() => of([])));
  }

  private fetchAccountCentros(noCia: number): Observable<ActiveCentro[]> {
    return this.http
      .get<ActiveCentro[]>(this.applicationConfigService.getEndpointFor(`api/account/centros?noCia=${noCia}`))
      .pipe(catchError(() => of([])));
  }

  private fetchAccountBodegas(centroId: number): Observable<ActiveBodega[]> {
    return this.http
      .get<ActiveBodega[]>(this.applicationConfigService.getEndpointFor(`api/account/bodegas?centroId=${centroId}`))
      .pipe(catchError(() => of([])));
  }

  private navigateToStoredUrl(): void {
    const previousUrl = this.stateStorageService.getUrl();
    if (previousUrl) {
      this.stateStorageService.clearUrl();
      this.router.navigateByUrl(previousUrl);
    }
  }
}
