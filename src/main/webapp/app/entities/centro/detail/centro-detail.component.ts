import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICentro } from '../centro.model';

@Component({
  selector: 'jhi-centro-detail',
  templateUrl: './centro-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CentroDetailComponent {
  centro = input<ICentro | null>(null);

  previousState(): void {
    window.history.back();
  }
}
