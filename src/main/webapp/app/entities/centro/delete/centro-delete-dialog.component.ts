import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICentro } from '../centro.model';
import { CentroService } from '../service/centro.service';

@Component({
  templateUrl: './centro-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CentroDeleteDialogComponent {
  centro?: ICentro;

  protected centroService = inject(CentroService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.centroService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
