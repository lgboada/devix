import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBodega } from '../bodega.model';
import { BodegaService } from '../service/bodega.service';

@Component({
  templateUrl: './bodega-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BodegaDeleteDialogComponent {
  bodega?: IBodega;

  protected bodegaService = inject(BodegaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bodegaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
