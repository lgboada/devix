import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IUsuarioCentro } from '../usuario-centro.model';

@Component({
  selector: 'jhi-usuario-centro-detail',
  templateUrl: './usuario-centro-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class UsuarioCentroDetailComponent {
  usuarioCentro = input<IUsuarioCentro | null>(null);

  previousState(): void {
    window.history.back();
  }
}
