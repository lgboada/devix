import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITipoCatalogo } from '../tipo-catalogo.model';
import { TipoCatalogoService } from '../service/tipo-catalogo.service';

@Component({
  templateUrl: './tipo-catalogo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TipoCatalogoDeleteDialogComponent {
  tipoCatalogo?: ITipoCatalogo;

  protected tipoCatalogoService = inject(TipoCatalogoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoCatalogoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
