import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITipoEvento } from '../tipo-evento.model';

@Component({
  selector: 'jhi-tipo-evento-detail',
  templateUrl: './tipo-evento-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TipoEventoDetailComponent {
  tipoEvento = input<ITipoEvento | null>(null);

  previousState(): void {
    window.history.back();
  }
}
