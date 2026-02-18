import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDetalleFactura } from '../detalle-factura.model';

@Component({
  selector: 'jhi-detalle-factura-detail',
  templateUrl: './detalle-factura-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DetalleFacturaDetailComponent {
  detalleFactura = input<IDetalleFactura | null>(null);

  previousState(): void {
    window.history.back();
  }
}
