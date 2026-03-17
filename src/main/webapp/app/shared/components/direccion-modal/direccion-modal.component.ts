import { Component, Input, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize, map } from 'rxjs/operators';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { IDireccion, NewDireccion } from 'app/entities/direccion/direccion.model';
import { DireccionService } from 'app/entities/direccion/service/direccion.service';
import { ITipoDireccion } from 'app/entities/tipo-direccion/tipo-direccion.model';
import { TipoDireccionService } from 'app/entities/tipo-direccion/service/tipo-direccion.service';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { CiudadService } from 'app/entities/ciudad/service/ciudad.service';
import { IProvincia } from 'app/entities/provincia/provincia.model';
import { ProvinciaService } from 'app/entities/provincia/service/provincia.service';

@Component({
  selector: 'jhi-direccion-modal',
  templateUrl: './direccion-modal.component.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DireccionModalComponent implements OnInit {
  @Input() clienteId!: number;
  @Input() direccion: IDireccion | null = null;

  isSaving = false;
  saveError: string | null = null;
  tipoDireccions: ITipoDireccion[] = [];
  provincias: IProvincia[] = [];
  filteredCiudads: ICiudad[] = [];

  protected readonly activeModal = inject(NgbActiveModal);
  protected readonly direccionService = inject(DireccionService);
  protected readonly tipoDireccionService = inject(TipoDireccionService);
  protected readonly ciudadService = inject(CiudadService);
  protected readonly provinciaService = inject(ProvinciaService);
  protected readonly activeCompanyService = inject(ActiveCompanyService);
  private readonly fb = inject(FormBuilder);
  private initialSelectedCiudadId: number | null = null;

  editForm = this.fb.group({
    id: [null as number | null],
    tipoDireccion: [null as ITipoDireccion | null, Validators.required],
    provinciaId: [null as number | null],
    ciudad: [null as ICiudad | null, Validators.required],
    descripcion: ['', Validators.required],
    telefono: [''],
    latitud: [null as number | null],
    longitud: [null as number | null],
  });

  compareTipoDireccion = (o1: Pick<ITipoDireccion, 'id'> | null, o2: Pick<ITipoDireccion, 'id'> | null): boolean =>
    this.tipoDireccionService.compareTipoDireccion(o1, o2);
  compareCiudad = (o1: Pick<ICiudad, 'id'> | null, o2: Pick<ICiudad, 'id'> | null): boolean => this.ciudadService.compareCiudad(o1, o2);

  ngOnInit(): void {
    this.initialSelectedCiudadId = this.direccion?.ciudad?.id ?? null;
    this.editForm.controls.provinciaId.valueChanges.subscribe(provinciaId => {
      this.loadCitiesForProvince(provinciaId ?? null, this.initialSelectedCiudadId);
      this.initialSelectedCiudadId = null;
    });

    if (this.direccion) {
      this.editForm.patchValue({
        id: this.direccion.id,
        descripcion: this.direccion.descripcion ?? '',
        telefono: this.direccion.telefono ?? '',
        latitud: this.direccion.latitud ?? null,
        longitud: this.direccion.longitud ?? null,
      });
    }

    this.tipoDireccionService
      .query({ sort: ['descripcion,asc'] })
      .pipe(map((res: HttpResponse<ITipoDireccion[]>) => res.body ?? []))
      .subscribe(tipos => {
        this.tipoDireccions = tipos;
        const tipoId = this.direccion?.tipoDireccion?.id;
        if (tipoId) {
          const matchedTipo = tipos.find(tipo => tipo.id === tipoId) ?? null;
          this.editForm.patchValue({ tipoDireccion: matchedTipo });
        }
      });

    this.provinciaService
      .query({ sort: ['descripcion,asc'] })
      .pipe(map((res: HttpResponse<IProvincia[]>) => res.body ?? []))
      .subscribe(provincias => {
        this.provincias = provincias;
        const selectedCiudadId = this.direccion?.ciudad?.id ?? null;
        if (selectedCiudadId) {
          this.ciudadService
            .find(selectedCiudadId)
            .pipe(map((res: HttpResponse<ICiudad>) => res.body))
            .subscribe(ciudad => {
              const provinciaId = ciudad?.provincia?.id ?? null;
              this.editForm.patchValue({ provinciaId });
            });
          return;
        }
        this.filteredCiudads = [];
      });
  }

  guardar(): void {
    if (this.editForm.invalid || !this.clienteId) {
      this.editForm.markAllAsTouched();
      return;
    }

    const noCia = this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
    if (noCia === null) {
      this.saveError = 'No se pudo determinar la compania activa.';
      return;
    }

    this.isSaving = true;
    this.saveError = null;

    const formValue = this.editForm.getRawValue();
    const ciudadId = formValue.ciudad?.id ?? null;
    if (ciudadId === null) {
      this.isSaving = false;
      this.saveError = 'Debe seleccionar una ciudad valida.';
      return;
    }

    const payload = {
      id: formValue.id,
      noCia,
      descripcion: formValue.descripcion ?? '',
      telefono: formValue.telefono ?? null,
      latitud: formValue.latitud ?? null,
      longitud: formValue.longitud ?? null,
      tipoDireccion: formValue.tipoDireccion ? { id: formValue.tipoDireccion.id } : null,
      ciudad: { id: ciudadId },
      cliente: { id: this.clienteId },
    };

    const request$ =
      payload.id !== null
        ? this.direccionService.update(payload as IDireccion)
        : this.direccionService.create({ ...(payload as Omit<NewDireccion, 'id'>), id: null });

    request$.pipe(finalize(() => (this.isSaving = false))).subscribe({
      next: () => this.activeModal.close('saved'),
      error: () => {
        this.saveError = 'No se pudo guardar la direccion. Intenta nuevamente.';
      },
    });
  }

  cancelar(): void {
    this.activeModal.dismiss('cancel');
  }

  private loadCitiesForProvince(provinciaId: number | null, selectedCiudadId: number | null): void {
    if (!provinciaId) {
      this.filteredCiudads = [];
      this.editForm.patchValue({ ciudad: null });
      return;
    }

    this.ciudadService
      .query({
        'provinciaId.equals': provinciaId,
        sort: ['descripcion,asc'],
      })
      .pipe(map((res: HttpResponse<ICiudad[]>) => res.body ?? []))
      .subscribe(ciudads => {
        this.filteredCiudads = ciudads;
        if (selectedCiudadId) {
          const selectedCiudad = ciudads.find(ciudad => ciudad.id === selectedCiudadId) ?? null;
          this.editForm.patchValue({ ciudad: selectedCiudad });
          return;
        }
        this.editForm.patchValue({ ciudad: null });
      });
  }
}
