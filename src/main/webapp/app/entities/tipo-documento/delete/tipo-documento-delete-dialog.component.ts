import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITipoDocumento } from '../tipo-documento.model';
import { TipoDocumentoService } from '../service/tipo-documento.service';

@Component({
  templateUrl: './tipo-documento-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TipoDocumentoDeleteDialogComponent {
  tipoDocumento?: ITipoDocumento;

  protected tipoDocumentoService = inject(TipoDocumentoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(noCia: number, codigo: string): void {
    this.tipoDocumentoService.delete(noCia, codigo).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
