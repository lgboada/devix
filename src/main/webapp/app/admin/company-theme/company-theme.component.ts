import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import SharedModule from 'app/shared/shared.module';
import { CompanyThemeAdminService, CompanyThemeResponse } from './company-theme-admin.service';

@Component({
  selector: 'jhi-company-theme',
  templateUrl: './company-theme.component.html',
  imports: [SharedModule, FormsModule],
})
export default class CompanyThemeComponent implements OnInit {
  private readonly companyThemeAdminService = inject(CompanyThemeAdminService);
  private readonly activeCompanyService = inject(ActiveCompanyService);
  private readonly accountService = inject(AccountService);

  readonly themes = signal<string[]>([]);
  readonly selectedTheme = signal<string>('litera');
  readonly currentTheme = signal<CompanyThemeResponse | null>(null);
  readonly activeCompany = this.activeCompanyService.trackActiveCompany();
  readonly availableCompanies = this.activeCompanyService.trackAvailableCompanies();
  readonly savingTheme = signal<boolean>(false);
  readonly uploadingAssets = signal<boolean>(false);
  readonly message = signal<string>('');
  readonly error = signal<string>('');

  readonly canUpload = computed(() => !this.uploadingAssets() && (!!this.logoFile() || !!this.backgroundFile()));

  private readonly logoFile = signal<File | null>(null);
  private readonly backgroundFile = signal<File | null>(null);

  ngOnInit(): void {
    // Recarga empresas desde el servidor (evita lista obsoleta si se añadieron usuario-centro tras el login).
    this.accountService.identity(true).subscribe(() => this.initThemeData());
  }

  private initThemeData(): void {
    this.loadCurrentTheme();
    this.companyThemeAdminService.getThemes().subscribe({
      next: response => this.themes.set(response.themes ?? ['litera']),
      error: () => this.themes.set(['litera']),
    });
  }

  onCompanyChange(noCiaRaw: string): void {
    const noCia = Number(noCiaRaw);
    if (!Number.isFinite(noCia)) {
      return;
    }
    this.activeCompanyService.selectCompany(noCia);
    this.logoFile.set(null);
    this.backgroundFile.set(null);
    this.clearMessages();
    this.loadCurrentTheme();
  }

  onLogoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.logoFile.set(input.files?.[0] ?? null);
  }

  onBackgroundSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.backgroundFile.set(input.files?.[0] ?? null);
  }

  saveTheme(): void {
    this.clearMessages();
    this.savingTheme.set(true);
    this.companyThemeAdminService
      .updateTheme(this.selectedTheme())
      .pipe(finalize(() => this.savingTheme.set(false)))
      .subscribe({
        next: response => {
          this.currentTheme.set(response);
          this.selectedTheme.set(response.themeName);
          this.message.set('Template actualizado correctamente.');
        },
        error: () => {
          this.error.set('No se pudo actualizar el template.');
        },
      });
  }

  uploadAssets(): void {
    if (!this.canUpload()) {
      return;
    }
    this.clearMessages();
    this.uploadingAssets.set(true);
    this.companyThemeAdminService
      .uploadAssets(this.logoFile() ?? undefined, this.backgroundFile() ?? undefined)
      .pipe(finalize(() => this.uploadingAssets.set(false)))
      .subscribe({
        next: response => {
          this.currentTheme.set(response);
          this.logoFile.set(null);
          this.backgroundFile.set(null);
          this.message.set('Assets actualizados correctamente.');
        },
        error: () => {
          this.error.set('No se pudo subir logo/background. Verifica formato de imagen.');
        },
      });
  }

  private loadCurrentTheme(): void {
    this.companyThemeAdminService.getCurrent().subscribe({
      next: response => {
        this.currentTheme.set(response);
        this.selectedTheme.set(response.themeName || 'litera');
      },
      error: () => {
        this.currentTheme.set({ themeName: 'litera', logoUrl: null, backgroundUrl: null });
        this.selectedTheme.set('litera');
      },
    });
  }

  private clearMessages(): void {
    this.message.set('');
    this.error.set('');
  }
}
