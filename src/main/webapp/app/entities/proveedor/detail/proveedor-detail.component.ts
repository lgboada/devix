import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IProveedor } from '../proveedor.model';

@Component({
  selector: 'jhi-proveedor-detail',
  templateUrl: './proveedor-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ProveedorDetailComponent {
  proveedor = input<IProveedor | null>(null);

  previousState(): void {
    window.history.back();
  }
}
