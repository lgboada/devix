import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICiudad } from '../ciudad.model';
import { CiudadService } from '../service/ciudad.service';

@Component({
  templateUrl: './ciudad-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CiudadDeleteDialogComponent {
  ciudad?: ICiudad;

  protected ciudadService = inject(CiudadService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ciudadService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
