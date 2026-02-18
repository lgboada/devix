import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumento } from '../documento.model';

@Component({
  selector: 'jhi-documento-detail',
  templateUrl: './documento-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentoDetailComponent {
  documento = input<IDocumento | null>(null);

  previousState(): void {
    window.history.back();
  }
}
