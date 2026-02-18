import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IFactura } from '../factura.model';

@Component({
  selector: 'jhi-factura-detail',
  templateUrl: './factura-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class FacturaDetailComponent {
  factura = input<IFactura | null>(null);

  previousState(): void {
    window.history.back();
  }
}
