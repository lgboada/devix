import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUsuarioCentro } from 'app/entities/usuario-centro/usuario-centro.model';
import { IUsuarioCentroBodega } from '../usuario-centro-bodega.model';
import { UsuarioCentroBodegaService, EntityArrayResponseType } from '../service/usuario-centro-bodega.service';
import { UsuarioCentroBodegaDeleteDialogComponent } from '../delete/usuario-centro-bodega-delete-dialog.component';

@Component({
  selector: 'jhi-usuario-centro-bodega-list',
  templateUrl: './usuario-centro-bodega-list.component.html',
  imports: [RouterModule, SharedModule],
})
export class UsuarioCentroBodegaListComponent implements OnInit {
  usuarioCentro: IUsuarioCentro | null = null;
  items = signal<IUsuarioCentroBodega[]>([]);
  isLoading = false;

  protected readonly router = inject(Router);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly service = inject(UsuarioCentroBodegaService);
  protected readonly modalService = inject(NgbModal);

  trackId = (item: IUsuarioCentroBodega): number => this.service.getIdentifier(item);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioCentro }) => {
      this.usuarioCentro = usuarioCentro ?? null;
      this.load();
    });
  }

  load(): void {
    const uc = this.usuarioCentro;
    if (!uc?.centro?.id || !uc?.user?.id) return;
    this.isLoading = true;
    this.service
      .query({
        'centroId.equals': uc.centro.id,
        'userId.equals': uc.user.id,
        sort: ['bodega.codigo,asc'],
      })
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.isLoading = false;
          this.items.set(res.body ?? []);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  delete(item: IUsuarioCentroBodega): void {
    const modalRef = this.modalService.open(UsuarioCentroBodegaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ucb = item;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  previousState(): void {
    if (this.usuarioCentro?.id) {
      this.router.navigate(['/usuario-centro', this.usuarioCentro.id, 'edit']);
    } else {
      window.history.back();
    }
  }
}
