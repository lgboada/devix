import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDetalleFactura } from '../detalle-factura.model';
import { DetalleFacturaService } from '../service/detalle-factura.service';

@Component({
  templateUrl: './detalle-factura-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DetalleFacturaDeleteDialogComponent {
  detalleFactura?: IDetalleFactura;

  protected detalleFacturaService = inject(DetalleFacturaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detalleFacturaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
