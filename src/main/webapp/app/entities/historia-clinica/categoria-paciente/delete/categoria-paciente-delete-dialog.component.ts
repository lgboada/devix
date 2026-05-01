import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICategoriaPaciente } from '../categoria-paciente.model';
import { CategoriaPacienteService } from '../service/categoria-paciente.service';

@Component({
  templateUrl: './categoria-paciente-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CategoriaPacienteDeleteDialogComponent {
  categoriaPaciente?: ICategoriaPaciente;

  protected categoriaPacienteService = inject(CategoriaPacienteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categoriaPacienteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
