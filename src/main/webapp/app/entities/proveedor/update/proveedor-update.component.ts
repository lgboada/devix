import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';
import { ProveedorFormGroup, ProveedorFormService } from './proveedor-form.service';

@Component({
  selector: 'jhi-proveedor-update',
  templateUrl: './proveedor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProveedorUpdateComponent implements OnInit {
  isSaving = false;
  proveedor: IProveedor | null = null;

  protected proveedorService = inject(ProveedorService);
  protected proveedorFormService = inject(ProveedorFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProveedorFormGroup = this.proveedorFormService.createProveedorFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proveedor }) => {
      this.proveedor = proveedor;
      if (proveedor) {
        this.updateForm(proveedor);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proveedor = this.proveedorFormService.getProveedor(this.editForm);
    if (proveedor.id !== null) {
      this.subscribeToSaveResponse(this.proveedorService.update(proveedor));
    } else {
      this.subscribeToSaveResponse(this.proveedorService.create(proveedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProveedor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(proveedor: IProveedor): void {
    this.proveedor = proveedor;
    this.proveedorFormService.resetForm(this.editForm, proveedor);
  }
}
