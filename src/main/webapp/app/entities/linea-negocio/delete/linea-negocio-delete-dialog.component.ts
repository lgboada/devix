import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILineaNegocio } from '../linea-negocio.model';
import { LineaNegocioService } from '../service/linea-negocio.service';

@Component({
  templateUrl: './linea-negocio-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LineaNegocioDeleteDialogComponent {
  lineaNegocio?: ILineaNegocio;

  protected lineaNegocioService = inject(LineaNegocioService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(noCia: number, lineaNo: string): void {
    this.lineaNegocioService.delete(noCia, lineaNo).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
