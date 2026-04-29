import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICentro } from 'app/entities/centro/centro.model';
import { IBodega } from '../bodega.model';
import { BodegaService, EntityArrayResponseType } from '../service/bodega.service';
import { BodegaDeleteDialogComponent } from '../delete/bodega-delete-dialog.component';

@Component({
  selector: 'jhi-bodega-list-centro',
  templateUrl: './bodega-list-centro.component.html',
  imports: [RouterModule, SharedModule],
})
export class BodegaListCentroComponent implements OnInit {
  centro: ICentro | null = null;
  bodegas = signal<IBodega[]>([]);
  isLoading = false;

  protected readonly router = inject(Router);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly bodegaService = inject(BodegaService);
  protected readonly modalService = inject(NgbModal);

  trackId = (item: IBodega): number => this.bodegaService.getBodegaIdentifier(item);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ centro }) => {
      this.centro = centro ?? null;
      this.load();
    });
  }

  load(): void {
    const centroId = this.centro?.id;
    if (!centroId) {
      return;
    }
    this.isLoading = true;
    this.bodegaService
      .query({
        'centroId.equals': centroId,
        sort: ['codigo,asc'],
      })
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.isLoading = false;
          this.bodegas.set(res.body ?? []);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  delete(bodega: IBodega): void {
    const modalRef = this.modalService.open(BodegaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bodega = bodega;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  previousState(): void {
    if (this.centro?.id) {
      this.router.navigate(['/centro', this.centro.id, 'view']);
    } else {
      window.history.back();
    }
  }
}
