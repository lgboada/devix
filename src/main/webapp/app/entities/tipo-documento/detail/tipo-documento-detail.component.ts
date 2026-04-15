import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITipoDocumento } from '../tipo-documento.model';

@Component({
  selector: 'jhi-tipo-documento-detail',
  templateUrl: './tipo-documento-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TipoDocumentoDetailComponent {
  tipoDocumento = input<ITipoDocumento | null>(null);

  previousState(): void {
    window.history.back();
  }
}
