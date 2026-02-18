import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITipoCliente } from '../tipo-cliente.model';
import { TipoClienteService } from '../service/tipo-cliente.service';

@Component({
  templateUrl: './tipo-cliente-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TipoClienteDeleteDialogComponent {
  tipoCliente?: ITipoCliente;

  protected tipoClienteService = inject(TipoClienteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoClienteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
