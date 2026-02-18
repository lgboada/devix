import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITipoCliente } from '../tipo-cliente.model';

@Component({
  selector: 'jhi-tipo-cliente-detail',
  templateUrl: './tipo-cliente-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TipoClienteDetailComponent {
  tipoCliente = input<ITipoCliente | null>(null);

  previousState(): void {
    window.history.back();
  }
}
