import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICategoriaPaciente } from '../categoria-paciente.model';

@Component({
  selector: 'jhi-categoria-paciente-detail',
  templateUrl: './categoria-paciente-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CategoriaPacienteDetailComponent {
  categoriaPaciente = input<ICategoriaPaciente | null>(null);

  previousState(): void {
    window.history.back();
  }
}
