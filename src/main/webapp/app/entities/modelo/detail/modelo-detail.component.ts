import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IModelo } from '../modelo.model';

@Component({
  selector: 'jhi-modelo-detail',
  templateUrl: './modelo-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ModeloDetailComponent {
  modelo = input<IModelo | null>(null);

  previousState(): void {
    window.history.back();
  }
}
