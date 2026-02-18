import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IEmpleado } from '../empleado.model';

@Component({
  selector: 'jhi-empleado-detail',
  templateUrl: './empleado-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class EmpleadoDetailComponent {
  empleado = input<IEmpleado | null>(null);

  previousState(): void {
    window.history.back();
  }
}
