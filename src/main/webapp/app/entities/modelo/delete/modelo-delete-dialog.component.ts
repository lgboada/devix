import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IModelo } from '../modelo.model';
import { ModeloService } from '../service/modelo.service';

@Component({
  templateUrl: './modelo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ModeloDeleteDialogComponent {
  modelo?: IModelo;

  protected modeloService = inject(ModeloService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.modeloService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
