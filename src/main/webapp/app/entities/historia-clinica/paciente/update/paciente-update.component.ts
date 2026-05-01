import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { FormsModule, ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';

import SharedModule from 'app/shared/shared.module';
import dayjs from 'dayjs/esm';
import { FileService } from 'app/shared/service/file.service';
import { CatalogoService } from 'app/entities/catalogo/service/catalogo.service';
import { TipoCatalogoService } from 'app/entities/tipo-catalogo/service/tipo-catalogo.service';
import { ITipoCatalogo } from 'app/entities/tipo-catalogo/tipo-catalogo.model';
import { CiudadService } from 'app/entities/ciudad/service/ciudad.service';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { ProvinciaService } from 'app/entities/provincia/service/provincia.service';
import { IProvincia } from 'app/entities/provincia/provincia.model';

import { IPaciente, NewPaciente } from '../paciente.model';
import { PacienteService } from '../service/paciente.service';
import { ICategoriaPaciente } from '../../categoria-paciente/categoria-paciente.model';
import { CategoriaPacienteService } from '../../categoria-paciente/service/categoria-paciente.service';

interface CatalogOption {
  code: string;
  label: string;
}

@Component({
  selector: 'jhi-paciente-update',
  templateUrl: './paciente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PacienteUpdateComponent implements OnInit {
  isSaving = false;
  isUploading = false;
  uploadError: string | null = null;
  paciente: IPaciente | null = null;
  categoriasPacienteSharedCollection: ICategoriaPaciente[] = [];
  tipoDniOptions: CatalogOption[] = [];
  sexoOptions: CatalogOption[] = [];
  estadoCivilOptions: CatalogOption[] = [];
  grupoSanguineoOptions: CatalogOption[] = [];
  lateralidadOptions: CatalogOption[] = [];
  tipoDiscapacidadOptions: CatalogOption[] = [];
  prioritarioOptions: CatalogOption[] = [];
  religionOptions: CatalogOption[] = [];
  provinciasSharedCollection: IProvincia[] = [];
  filteredCiudadsCollection: ICiudad[] = [];
  selectedProvinciaId: number | null = null;
  orientacionGeneroOptions: CatalogOption[] = [];
  identidadSexualOptions: CatalogOption[] = [];

  protected pacienteService = inject(PacienteService);
  protected categoriaPacienteService = inject(CategoriaPacienteService);
  protected activatedRoute = inject(ActivatedRoute);
  protected fileService = inject(FileService);
  protected tipoCatalogoService = inject(TipoCatalogoService);
  protected catalogoService = inject(CatalogoService);
  protected ciudadService = inject(CiudadService);
  protected provinciaService = inject(ProvinciaService);

  editForm = new FormGroup({
    id: new FormControl<IPaciente['id'] | null>({ value: null, disabled: true }),
    noCia: new FormControl<number | null>(null),
    dni: new FormControl<string | null>(null, [Validators.required, Validators.maxLength(50)]),
    tipoDni: new FormControl<string | null>(null, [Validators.maxLength(1)]),
    nombres: new FormControl<string | null>(null, [Validators.required, Validators.maxLength(200)]),
    apellidos: new FormControl<string | null>(null, [Validators.required, Validators.maxLength(200)]),
    titulo: new FormControl<string | null>(null, [Validators.maxLength(250)]),
    fechaNacimiento: new FormControl<dayjs.Dayjs | null>(null),
    sexo: new FormControl<string | null>(null, [Validators.maxLength(2)]),
    orientacionGenero: new FormControl<string | null>(null),
    identidadSexual: new FormControl<string | null>(null),
    grupoSanguineo: new FormControl<string | null>(null, [Validators.maxLength(5)]),
    estadoCivil: new FormControl<string | null>(null, [Validators.maxLength(1)]),
    nivelEstudio: new FormControl<string | null>(null),
    ocupacion: new FormControl<string | null>(null),
    religion: new FormControl<string | null>(null),
    tipoDiscapacidad: new FormControl<string | null>(null),
    porcentajeDiscapacidad: new FormControl<string | null>(null),
    lateralidad: new FormControl<string | null>(null),
    foto: new FormControl<string | null>(null),
    numeroHistoria: new FormControl<string | null>(null, [Validators.maxLength(15)]),
    tipoHistoria: new FormControl<string | null>(null, [Validators.maxLength(30)]),
    prioritario: new FormControl<string | null>(null),
    comentario: new FormControl<string | null>(null, [Validators.maxLength(255)]),
    actividades: new FormControl<string | null>(null),
    agencia: new FormControl<string | null>(null),
    area: new FormControl<string | null>(null),
    ciiu: new FormControl<string | null>(null),
    fechaIngreso: new FormControl<dayjs.Dayjs | null>(null),
    fechaEgreso: new FormControl<dayjs.Dayjs | null>(null),
    motivoSalida: new FormControl<string | null>(null, [Validators.maxLength(30)]),
    activo: new FormControl<boolean | null>(true),
    estado: new FormControl<string | null>(null, [Validators.maxLength(3)]),
    fechaCreacion: new FormControl<dayjs.Dayjs | null>(null),
    ciudad: new FormControl<{ id: number } | null>(null),
    categoriaPaciente: new FormControl<ICategoriaPaciente | null>(null),
  });

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paciente }) => {
      this.paciente = paciente;
      if (paciente) {
        this.updateForm(paciente);
      }
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const raw = this.editForm.getRawValue();
    if (raw.id !== null) {
      this.subscribeToSaveResponse(this.pacienteService.update(raw as IPaciente));
    } else {
      this.subscribeToSaveResponse(this.pacienteService.create(raw as NewPaciente));
    }
  }

  compareCategoriaPaciente = (o1: ICategoriaPaciente | null, o2: ICategoriaPaciente | null): boolean =>
    this.categoriaPacienteService.compareCategoriaPaciente(o1, o2);

  compareCiudad = (o1: ICiudad | null, o2: ICiudad | null): boolean => this.ciudadService.compareCiudad(o1, o2);

  onProvinciaChange(value: string | number | null): void {
    const provinciaId = Number(value);
    this.selectedProvinciaId = Number.isFinite(provinciaId) && provinciaId > 0 ? provinciaId : null;
    this.loadCitiesForSelectedProvince();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const selectedFile = input.files?.[0];
    if (!selectedFile) return;

    this.uploadError = null;
    this.isUploading = true;

    this.fileService
      .upload(selectedFile)
      .pipe(finalize(() => (this.isUploading = false)))
      .subscribe({
        next: response => {
          const storedFilename = response.body?.filename;
          if (!storedFilename) {
            this.uploadError = 'No se pudo obtener el nombre del archivo subido.';
            return;
          }
          this.editForm.patchValue({ foto: storedFilename });
          this.editForm.get('foto')?.markAsDirty();
          this.editForm.get('foto')?.markAsTouched();
        },
        error: () => {
          this.uploadError = 'No se pudo subir la imagen. Intenta nuevamente.';
        },
      });
  }

  getCurrentImageUrl(): string | null {
    const foto = this.editForm.get('foto')?.value;
    return foto ? this.fileService.getFileUrl(foto) : null;
  }

  protected updateForm(paciente: IPaciente): void {
    this.editForm.patchValue({ ...paciente });
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaPacienteService
      .query({ size: 999 })
      .pipe(map((res: HttpResponse<ICategoriaPaciente[]>) => res.body ?? []))
      .subscribe(cats => {
        this.categoriasPacienteSharedCollection = cats;
      });

    this.provinciaService
      .query({ sort: ['descripcion,asc'] })
      .pipe(map((res: HttpResponse<IProvincia[]>) => res.body ?? []))
      .subscribe(provincias => {
        this.provinciasSharedCollection = provincias;
      });

    this.initializeDependentCitySelection();

    this.loadTipoDniOptions();
    this.loadSexoOptions();
    this.loadEstadoCivilOptions();
    this.loadGrupoSanguineoOptions();
    this.loadLateralidadOptions();
    this.loadTipoDiscapacidadOptions();
    this.loadPrioritarioOptions();
    this.loadReligionOptions();
    this.loadOrientacionGeneroOptions();
    this.loadIdentidadSexualOptions();
  }

  private initializeDependentCitySelection(): void {
    const selectedCiudad = this.editForm.get('ciudad')?.value as ICiudad | { id: number } | null;
    const selectedCiudadId = selectedCiudad?.id ?? null;
    if (selectedCiudadId === null) {
      this.filteredCiudadsCollection = [];
      return;
    }
    this.ciudadService
      .find(selectedCiudadId)
      .pipe(map((res: HttpResponse<ICiudad>) => res.body))
      .subscribe(ciudad => {
        this.selectedProvinciaId = ciudad?.provincia?.id ?? null;
        this.loadCitiesForSelectedProvince(selectedCiudadId);
      });
  }

  private loadCitiesForSelectedProvince(selectedCiudadId?: number | null): void {
    if (this.selectedProvinciaId === null) {
      this.filteredCiudadsCollection = [];
      this.editForm.get('ciudad')?.setValue(null);
      return;
    }
    this.ciudadService
      .query({ 'provinciaId.equals': this.selectedProvinciaId, sort: ['descripcion,asc'] })
      .pipe(map((res: HttpResponse<ICiudad[]>) => res.body ?? []))
      .subscribe(ciudads => {
        this.filteredCiudadsCollection = ciudads;
        if (selectedCiudadId) {
          const matched = ciudads.find(c => c.id === selectedCiudadId) ?? null;
          this.editForm.get('ciudad')?.setValue(matched, { emitEvent: false });
        } else {
          this.editForm.get('ciudad')?.setValue(null);
        }
      });
  }

  private loadReligionOptions(): void {
    this.loadCatalogOptions(['RELIGION'], opts => (this.religionOptions = opts));
  }

  private loadPrioritarioOptions(): void {
    this.loadCatalogOptions(['ATENCION_PRIORITARIA', 'PRIORITARIO'], opts => (this.prioritarioOptions = opts));
  }

  private loadTipoDiscapacidadOptions(): void {
    this.loadCatalogOptions(['TIPO_DISAPACIDAD', 'TIPO_DISCAPACIDAD'], opts => (this.tipoDiscapacidadOptions = opts));
  }

  private loadLateralidadOptions(): void {
    this.loadCatalogOptions(['LATERALIDAD'], opts => (this.lateralidadOptions = opts));
  }

  private loadGrupoSanguineoOptions(): void {
    this.loadCatalogOptions(['TIPO_DE_SANGRE', 'TIPO_SANGRE', 'SANGRE'], opts => (this.grupoSanguineoOptions = opts));
  }

  private loadEstadoCivilOptions(): void {
    this.loadCatalogOptions(['ESTADO_CIVIL', 'ESTADO_CIVILIDAD'], opts => (this.estadoCivilOptions = opts));
  }

  private loadSexoOptions(): void {
    this.loadCatalogOptions(['SEXO'], opts => (this.sexoOptions = opts));
  }

  private loadTipoDniOptions(): void {
    this.loadCatalogOptions(['TIPO_DOCUMENTO', 'TIPO_DOC', 'DOCUMENTO'], opts => (this.tipoDniOptions = opts));
  }

  private loadOrientacionGeneroOptions(): void {
    this.loadCatalogOptions(['ORIENTACION_SEXUAL', 'ORIENTACION_GENERO'], opts => (this.orientacionGeneroOptions = opts));
  }

  private loadIdentidadSexualOptions(): void {
    this.loadCatalogOptions(['IDENTIDAD_SEXUAL'], opts => (this.identidadSexualOptions = opts));
  }

  private loadCatalogOptions(keys: string[], onLoaded: (opts: CatalogOption[]) => void): void {
    this.tipoCatalogoService.query().subscribe((res: HttpResponse<ITipoCatalogo[]>) => {
      const tipos = res.body ?? [];
      const matched = tipos.find(t =>
        [t.descripcion ?? '', t.categoria ?? ''].map(v => v.trim().toUpperCase().replace(/\s+/g, '_')).some(v => keys.includes(v)),
      );
      if (!matched?.id) return;
      this.catalogoService.query({ 'tipoCatalogoId.equals': matched.id, sort: ['orden,asc', 'descripcion1,asc'] }).subscribe(catRes => {
        onLoaded(
          (catRes.body ?? [])
            .map(item => ({ code: (item.texto1 ?? '').trim(), label: (item.descripcion1 ?? '').trim() }))
            .filter(o => o.code),
        );
      });
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaciente>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // handled by alert component
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
