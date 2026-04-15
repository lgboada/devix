import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormsModule } from '@angular/forms';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { ILineaNegocio } from '../linea-negocio.model';
import { EntityArrayResponseType, LineaNegocioService } from '../service/linea-negocio.service';
import { LineaNegocioDeleteDialogComponent } from '../delete/linea-negocio-delete-dialog.component';

@Component({
  selector: 'jhi-linea-negocio',
  templateUrl: './linea-negocio.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective],
})
export class LineaNegocioComponent implements OnInit {
  subscription: Subscription | null = null;
  lineaNegocios = signal<ILineaNegocio[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});

  public readonly router = inject(Router);
  protected readonly lineaNegocioService = inject(LineaNegocioService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected readonly activeCompanyService = inject(ActiveCompanyService);

  trackKey = (item: ILineaNegocio): string => this.lineaNegocioService.getKey(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  delete(lineaNegocio: ILineaNegocio): void {
    const modalRef = this.modalService.open(LineaNegocioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lineaNegocio = lineaNegocio;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => this.onResponseSuccess(res),
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.lineaNegocios.set(this.refineData(response.body ?? []));
  }

  protected refineData(data: ILineaNegocio[]): ILineaNegocio[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const noCia = this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
    return this.lineaNegocioService
      .query({ sort: this.sortService.buildSortParam(this.sortState()), noCia })
      .pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: { sort: this.sortService.buildSortParam(sortState) },
      });
    });
  }
}
