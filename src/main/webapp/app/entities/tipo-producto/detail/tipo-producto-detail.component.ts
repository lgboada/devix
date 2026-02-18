import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITipoProducto } from '../tipo-producto.model';

@Component({
  selector: 'jhi-tipo-producto-detail',
  templateUrl: './tipo-producto-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TipoProductoDetailComponent {
  tipoProducto = input<ITipoProducto | null>(null);

  previousState(): void {
    window.history.back();
  }
}
