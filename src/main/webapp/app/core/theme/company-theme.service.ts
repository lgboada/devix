import { Injectable, effect, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, of, switchMap } from 'rxjs';

import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { CompanyTheme } from 'app/core/theme/company-theme.model';

const DEFAULT_THEME: CompanyTheme = {
  themeName: 'litera',
  logoUrl: null,
  backgroundUrl: null,
};

@Injectable({ providedIn: 'root' })
export class CompanyThemeService {
  private readonly http = inject(HttpClient);
  private readonly activeCompanyService = inject(ActiveCompanyService);

  private readonly currentTheme = signal<CompanyTheme>(DEFAULT_THEME);

  constructor() {
    effect(() => {
      const activeCompany = this.activeCompanyService.trackActiveCompany()();
      if (!activeCompany) {
        this.setTheme(DEFAULT_THEME);
        return;
      }

      this.http
        .get<CompanyTheme>('/api/company-theme/current')
        .pipe(
          catchError(() => of(DEFAULT_THEME)),
          switchMap(theme => {
            this.setTheme(theme);
            return of(theme);
          }),
        )
        .subscribe();
    });
  }

  trackTheme() {
    return this.currentTheme.asReadonly();
  }

  private setTheme(theme: CompanyTheme): void {
    const normalized = {
      themeName: theme.themeName ?? 'litera',
      logoUrl: theme.logoUrl ?? null,
      backgroundUrl: theme.backgroundUrl ?? null,
    };
    this.currentTheme.set(normalized);
    this.applyThemeCss(normalized.themeName);
    this.applyBackground(normalized.backgroundUrl);
  }

  private applyThemeCss(themeName: string): void {
    const link = document.getElementById('company-theme') as HTMLLinkElement | null;
    if (!link) {
      return;
    }
    link.href = `content/themes/${encodeURIComponent(themeName)}.min.css`;
  }

  private applyBackground(backgroundUrl: string | null): void {
    const body = document.body;
    if (!backgroundUrl) {
      body.style.removeProperty('background-image');
      body.style.removeProperty('background-size');
      body.style.removeProperty('background-repeat');
      body.style.removeProperty('background-position');
      return;
    }
    body.style.setProperty('background-image', `url("${backgroundUrl}")`);
    body.style.setProperty('background-size', 'cover');
    body.style.setProperty('background-repeat', 'no-repeat');
    body.style.setProperty('background-position', 'center');
  }
}
