import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class StateStorageService {
  private readonly previousUrlKey = 'previousUrl';
  private readonly authenticationKey = 'jhi-authenticationToken';
  private readonly localeKey = 'locale';
  private readonly activeCompanyNoCiaKey = 'activeCompanyNoCia';

  storeUrl(url: string): void {
    sessionStorage.setItem(this.previousUrlKey, JSON.stringify(url));
  }

  getUrl(): string | null {
    const previousUrl = sessionStorage.getItem(this.previousUrlKey);
    return previousUrl ? (JSON.parse(previousUrl) as string | null) : previousUrl;
  }

  clearUrl(): void {
    sessionStorage.removeItem(this.previousUrlKey);
  }

  storeAuthenticationToken(authenticationToken: string, rememberMe: boolean): void {
    authenticationToken = JSON.stringify(authenticationToken);
    this.clearAuthenticationToken();
    if (rememberMe) {
      localStorage.setItem(this.authenticationKey, authenticationToken);
    } else {
      sessionStorage.setItem(this.authenticationKey, authenticationToken);
    }
  }

  getAuthenticationToken(): string | null {
    const authenticationToken = localStorage.getItem(this.authenticationKey) ?? sessionStorage.getItem(this.authenticationKey);
    return authenticationToken ? (JSON.parse(authenticationToken) as string | null) : authenticationToken;
  }

  clearAuthenticationToken(): void {
    sessionStorage.removeItem(this.authenticationKey);
    localStorage.removeItem(this.authenticationKey);
  }

  storeLocale(locale: string): void {
    sessionStorage.setItem(this.localeKey, locale);
  }

  getLocale(): string | null {
    return sessionStorage.getItem(this.localeKey);
  }

  clearLocale(): void {
    sessionStorage.removeItem(this.localeKey);
  }

  storeActiveCompanyNoCia(noCia: number): void {
    sessionStorage.setItem(this.activeCompanyNoCiaKey, JSON.stringify(noCia));
  }

  getActiveCompanyNoCia(): number | null {
    const rawNoCia = sessionStorage.getItem(this.activeCompanyNoCiaKey);
    return rawNoCia ? (JSON.parse(rawNoCia) as number) : null;
  }

  clearActiveCompanyNoCia(): void {
    sessionStorage.removeItem(this.activeCompanyNoCiaKey);
  }
}
