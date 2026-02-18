import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICatalogo } from '../catalogo.model';

@Component({
  selector: 'jhi-catalogo-detail',
  templateUrl: './catalogo-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CatalogoDetailComponent {
  catalogo = input<ICatalogo | null>(null);

  previousState(): void {
    window.history.back();
  }
}
