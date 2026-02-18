import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMarca } from '../marca.model';

@Component({
  selector: 'jhi-marca-detail',
  templateUrl: './marca-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MarcaDetailComponent {
  marca = input<IMarca | null>(null);

  previousState(): void {
    window.history.back();
  }
}
