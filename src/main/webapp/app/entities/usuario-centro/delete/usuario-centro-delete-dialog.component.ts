import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUsuarioCentro } from '../usuario-centro.model';
import { UsuarioCentroService } from '../service/usuario-centro.service';

@Component({
  templateUrl: './usuario-centro-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UsuarioCentroDeleteDialogComponent {
  usuarioCentro?: IUsuarioCentro;

  protected usuarioCentroService = inject(UsuarioCentroService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.usuarioCentroService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
