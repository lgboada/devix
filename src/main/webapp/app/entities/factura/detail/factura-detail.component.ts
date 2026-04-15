import { Component, input, signal, inject } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IFactura } from '../factura.model';
import { FacturaService } from '../service/factura.service';

@Component({
  selector: 'jhi-factura-detail',
  templateUrl: './factura-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class FacturaDetailComponent {
  factura = input<IFactura | null>(null);

  enviandoSri = signal(false);
  resultadoSri = signal<any | null>(null);
  errorSri = signal<string | null>(null);

  protected facturaService = inject(FacturaService);

  previousState(): void {
    window.history.back();
  }

  enviarAlSri(): void {
    const f = this.factura();
    if (!f) return;
    this.enviandoSri.set(true);
    this.resultadoSri.set(null);
    this.errorSri.set(null);

    this.facturaService.enviarSri(f.id).subscribe({
      next: res => {
        this.resultadoSri.set(res.body);
        this.enviandoSri.set(false);
      },
      error: err => {
        this.errorSri.set(err?.error?.title ?? err?.message ?? 'Error al enviar al SRI');
        this.enviandoSri.set(false);
      },
    });
  }
}
