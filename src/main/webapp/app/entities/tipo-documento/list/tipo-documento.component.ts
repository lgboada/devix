import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormsModule } from '@angular/forms';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { ITipoDocumento } from '../tipo-documento.model';
import { EntityArrayResponseType, TipoDocumentoService } from '../service/tipo-documento.service';
import { TipoDocumentoDeleteDialogComponent } from '../delete/tipo-documento-delete-dialog.component';

@Component({
  selector: 'jhi-tipo-documento',
  templateUrl: './tipo-documento.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective],
})
export class TipoDocumentoComponent implements OnInit {
  subscription: Subscription | null = null;
  tipoDocumentos = signal<ITipoDocumento[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});

  public readonly router = inject(Router);
  protected readonly tipoDocumentoService = inject(TipoDocumentoService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected readonly activeCompanyService = inject(ActiveCompanyService);

  trackKey = (item: ITipoDocumento): string => this.tipoDocumentoService.getKey(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  delete(tipoDocumento: ITipoDocumento): void {
    const modalRef = this.modalService.open(TipoDocumentoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tipoDocumento = tipoDocumento;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.tipoDocumentos.set(this.refineData(dataFromBody));
  }

  protected refineData(data: ITipoDocumento[]): ITipoDocumento[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: ITipoDocumento[] | null): ITipoDocumento[] {
    return data ?? [];
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const noCia = this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
      noCia,
    };
    return this.tipoDocumentoService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = { sort: this.sortService.buildSortParam(sortState) };
    this.ngZone.run(() => {
      this.router.navigate(['./'], { relativeTo: this.activatedRoute, queryParams: queryParamsObj });
    });
  }
}
