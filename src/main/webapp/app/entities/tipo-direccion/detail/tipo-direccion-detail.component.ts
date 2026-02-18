import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITipoDireccion } from '../tipo-direccion.model';

@Component({
  selector: 'jhi-tipo-direccion-detail',
  templateUrl: './tipo-direccion-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TipoDireccionDetailComponent {
  tipoDireccion = input<ITipoDireccion | null>(null);

  previousState(): void {
    window.history.back();
  }
}
