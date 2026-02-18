import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICompania } from '../compania.model';

@Component({
  selector: 'jhi-compania-detail',
  templateUrl: './compania-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CompaniaDetailComponent {
  compania = input<ICompania | null>(null);

  previousState(): void {
    window.history.back();
  }
}
