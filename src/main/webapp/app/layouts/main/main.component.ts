import { Component, OnDestroy, OnInit, Renderer2, RendererFactory2, inject } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';
import { filter } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { CompanyThemeService } from 'app/core/theme/company-theme.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import FooterComponent from '../footer/footer.component';
import PageRibbonComponent from '../profiles/page-ribbon.component';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  providers: [AppPageTitleStrategy],
  imports: [RouterOutlet, FooterComponent, PageRibbonComponent],
})
export default class MainComponent implements OnInit, OnDestroy {
  private readonly renderer: Renderer2;
  private mutationObserver?: MutationObserver;
  private hideQueued = false;

  private readonly router = inject(Router);
  private readonly appPageTitleStrategy = inject(AppPageTitleStrategy);
  private readonly accountService = inject(AccountService);
  private readonly activeCompanyService = inject(ActiveCompanyService);
  private readonly companyThemeService = inject(CompanyThemeService);
  private readonly translateService = inject(TranslateService);
  private readonly rootRenderer = inject(RendererFactory2);

  constructor() {
    this.renderer = this.rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(() => {
      this.scheduleHideNoCiaFieldsAndColumns();
    });

    this.startNoCiaObserver();
    this.scheduleHideNoCiaFieldsAndColumns();

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
      this.scheduleHideNoCiaFieldsAndColumns();
    });
  }

  ngOnDestroy(): void {
    this.mutationObserver?.disconnect();
  }

  private startNoCiaObserver(): void {
    this.mutationObserver = new MutationObserver(() => {
      this.scheduleHideNoCiaFieldsAndColumns();
    });

    this.mutationObserver.observe(document.body, {
      childList: true,
      subtree: true,
    });
  }

  private scheduleHideNoCiaFieldsAndColumns(): void {
    if (this.hideQueued) {
      return;
    }
    this.hideQueued = true;
    requestAnimationFrame(() => {
      this.hideQueued = false;
      this.hideNoCiaFieldsAndColumns();
    });
  }

  private hideNoCiaFieldsAndColumns(): void {
    this.hideNoCiaFormField();
    this.hideNoCiaInDetails();
    this.hideNoCiaInTables();
  }

  private hideNoCiaFormField(): void {
    const activeCompany = this.activeCompanyService.trackActiveCompany()();
    const noCiaInputs = document.querySelectorAll('#field_noCia, input[data-cy="noCia"], [formcontrolname="noCia"]');
    noCiaInputs.forEach(inputElement => {
      const input = inputElement as HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement;
      if (input.hasAttribute('data-show-nocia')) {
        return;
      }
      if (activeCompany && input.value !== `${activeCompany.noCia}`) {
        this.renderer.setProperty(input, 'value', `${activeCompany.noCia}`);
        input.dispatchEvent(new Event('input', { bubbles: true }));
        input.dispatchEvent(new Event('change', { bubbles: true }));
      }

      const formGroup = input.closest('.mb-3');
      if (formGroup) {
        this.renderer.setStyle(formGroup, 'display', 'none');
      }
    });
  }

  private hideNoCiaInDetails(): void {
    const noCiaLabels = document.querySelectorAll('dt > span[jhitranslate$=".noCia"]');
    noCiaLabels.forEach(label => {
      const dt = label.closest('dt');
      const dd = dt?.nextElementSibling;
      if (dt) {
        this.renderer.setStyle(dt, 'display', 'none');
      }
      if (dd?.tagName === 'DD') {
        this.renderer.setStyle(dd, 'display', 'none');
      }
    });
  }

  private hideNoCiaInTables(): void {
    const tables = document.querySelectorAll('.table-entities table');
    tables.forEach(table => {
      const headers = Array.from(table.querySelectorAll('thead th')) as HTMLTableCellElement[];
      if (headers.length === 0) {
        return;
      }

      const noCiaIndexes = headers.filter(header => this.isNoCiaHeader(header)).map(header => header.cellIndex);

      noCiaIndexes.forEach(columnIndex => {
        table.querySelectorAll('tr').forEach(row => {
          const cells = row.querySelectorAll('th, td');
          const targetCell = cells.item(columnIndex);
          if (targetCell) {
            this.renderer.setStyle(targetCell, 'display', 'none');
          }
        });
      });
    });
  }

  private isNoCiaHeader(header: HTMLTableCellElement): boolean {
    const sortBy = (header.getAttribute('jhisortby') ?? header.getAttribute('jhiSortBy') ?? '').toLowerCase();
    if (sortBy === 'nocia') {
      return true;
    }

    const translatedNoCia = header.querySelector('span[jhitranslate$=".noCia"]');
    if (translatedNoCia) {
      return true;
    }

    return header.textContent?.replace(/\s+/g, '').toLowerCase().includes('nocia') ?? false;
  }
}
