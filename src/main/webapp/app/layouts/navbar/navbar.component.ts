import { Component, OnInit, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { ActiveCentroService } from 'app/core/auth/active-centro.service';
import { ActiveBodegaService } from 'app/core/auth/active-bodega.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { LANGUAGES } from 'app/config/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import { environment } from 'environments/environment';
import { CompanyThemeService } from 'app/core/theme/company-theme.service';
import ActiveMenuDirective from './active-menu.directive';
import NavbarItem from './navbar-item.model';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  imports: [RouterModule, SharedModule, HasAnyAuthorityDirective, ActiveMenuDirective],
})
export default class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = signal(true);
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';

  account = inject(AccountService).trackCurrentAccount();
  activeCompany = inject(ActiveCompanyService).trackActiveCompany();
  availableCompanies = inject(ActiveCompanyService).trackAvailableCompanies();
  activeCentro = inject(ActiveCentroService).trackActiveCentro();
  availableCentros = inject(ActiveCentroService).trackAvailableCentros();
  activeBodega = inject(ActiveBodegaService).trackActiveBodega();
  availableBodegas = inject(ActiveBodegaService).trackAvailableBodegas();
  theme = inject(CompanyThemeService).trackTheme();
  entitiesNavbarItems: NavbarItem[] = [];

  private readonly loginService = inject(LoginService);
  private readonly translateService = inject(TranslateService);
  private readonly stateStorageService = inject(StateStorageService);
  private readonly profileService = inject(ProfileService);
  private readonly accountService = inject(AccountService);
  private readonly activeCompanyService = inject(ActiveCompanyService);
  private readonly router = inject(Router);

  constructor() {
    const { VERSION } = environment;
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
  }

  changeLanguage(languageKey: string): void {
    this.stateStorageService.storeLocale(languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed.set(true);
  }

  login(): void {
    this.loginService.login();
  }

  logout(): void {
    this.collapseNavbar();
    this.activeCompanyService.clear();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  changeActiveCompany(noCia: number): void {
    this.accountService.selectCompanyAndReload(noCia).subscribe();
    this.collapseNavbar();
  }

  changeActiveCentro(centroId: number): void {
    this.accountService.selectCentroAndReload(centroId).subscribe();
    this.collapseNavbar();
  }

  changeActiveBodega(bodegaId: number): void {
    this.accountService.selectBodega(bodegaId);
    this.collapseNavbar();
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed.update(isNavbarCollapsed => !isNavbarCollapsed);
  }
}
