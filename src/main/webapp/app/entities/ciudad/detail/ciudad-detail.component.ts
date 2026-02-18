import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICiudad } from '../ciudad.model';

@Component({
  selector: 'jhi-ciudad-detail',
  templateUrl: './ciudad-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CiudadDetailComponent {
  ciudad = input<ICiudad | null>(null);

  previousState(): void {
    window.history.back();
  }
}
