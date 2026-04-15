import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ILineaNegocio } from '../linea-negocio.model';

@Component({
  selector: 'jhi-linea-negocio-detail',
  templateUrl: './linea-negocio-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class LineaNegocioDetailComponent {
  lineaNegocio = input<ILineaNegocio | null>(null);

  previousState(): void {
    window.history.back();
  }
}
