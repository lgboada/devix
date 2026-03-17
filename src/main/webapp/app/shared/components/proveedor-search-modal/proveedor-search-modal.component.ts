import { Component, Input, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { finalize, map } from 'rxjs/operators';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';

@Component({
  selector: 'jhi-proveedor-search-modal',
  templateUrl: './proveedor-search-modal.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProveedorSearchModalComponent implements OnInit {
  @Input() selectedProveedorId: number | null = null;
  @Input() title = 'Buscar proveedor';

  searchText = '';
  isLoading = false;
  errorMsg: string | null = null;
  proveedores: IProveedor[] = [];

  protected readonly activeModal = inject(NgbActiveModal);
  protected readonly proveedorService = inject(ProveedorService);

  ngOnInit(): void {
    this.buscar();
  }

  buscar(): void {
    this.errorMsg = null;
    this.isLoading = true;
    this.proveedorService
      .query({
        page: 1,
        size: 20,
        sort: ['nombre,asc'],
        ...(this.searchText.trim() ? { 'nombre.contains': this.searchText.trim() } : {}),
      })
      .pipe(
        map((res: HttpResponse<IProveedor[]>) => res.body ?? []),
        finalize(() => (this.isLoading = false)),
      )
      .subscribe({
        next: proveedors => (this.proveedores = proveedors),
        error: () => {
          this.proveedores = [];
          this.errorMsg = 'No se pudo cargar la lista de proveedores.';
        },
      });
  }

  seleccionar(proveedor: IProveedor): void {
    this.activeModal.close(proveedor);
  }

  cancelar(): void {
    this.activeModal.dismiss('cancel');
  }
}
