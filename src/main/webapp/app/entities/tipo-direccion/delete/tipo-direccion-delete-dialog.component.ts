import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITipoDireccion } from '../tipo-direccion.model';
import { TipoDireccionService } from '../service/tipo-direccion.service';

@Component({
  templateUrl: './tipo-direccion-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TipoDireccionDeleteDialogComponent {
  tipoDireccion?: ITipoDireccion;

  protected tipoDireccionService = inject(TipoDireccionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoDireccionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
