import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITipoCatalogo } from '../tipo-catalogo.model';

@Component({
  selector: 'jhi-tipo-catalogo-detail',
  templateUrl: './tipo-catalogo-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TipoCatalogoDetailComponent {
  tipoCatalogo = input<ITipoCatalogo | null>(null);

  previousState(): void {
    window.history.back();
  }
}
