import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUsuarioCentroBodega } from '../usuario-centro-bodega.model';
import { UsuarioCentroBodegaService } from '../service/usuario-centro-bodega.service';

@Component({
  templateUrl: './usuario-centro-bodega-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UsuarioCentroBodegaDeleteDialogComponent {
  ucb?: IUsuarioCentroBodega;

  protected readonly service = inject(UsuarioCentroBodegaService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.service.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
