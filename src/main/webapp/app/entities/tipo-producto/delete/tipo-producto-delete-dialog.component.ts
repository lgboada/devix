import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITipoProducto } from '../tipo-producto.model';
import { TipoProductoService } from '../service/tipo-producto.service';

@Component({
  templateUrl: './tipo-producto-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TipoProductoDeleteDialogComponent {
  tipoProducto?: ITipoProducto;

  protected tipoProductoService = inject(TipoProductoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoProductoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
