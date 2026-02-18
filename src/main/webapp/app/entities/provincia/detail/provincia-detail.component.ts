import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IProvincia } from '../provincia.model';

@Component({
  selector: 'jhi-provincia-detail',
  templateUrl: './provincia-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ProvinciaDetailComponent {
  provincia = input<IProvincia | null>(null);

  previousState(): void {
    window.history.back();
  }
}
