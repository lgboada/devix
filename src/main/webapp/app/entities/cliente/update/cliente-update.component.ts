import { Component, OnInit, effect, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { FileService } from 'app/shared/service/file.service';

import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { CiudadService } from 'app/entities/ciudad/service/ciudad.service';
import { IProvincia } from 'app/entities/provincia/provincia.model';
import { ProvinciaService } from 'app/entities/provincia/service/provincia.service';
import { CatalogoService } from 'app/entities/catalogo/service/catalogo.service';
import { TipoCatalogoService } from 'app/entities/tipo-catalogo/service/tipo-catalogo.service';
import { ITipoCatalogo } from 'app/entities/tipo-catalogo/tipo-catalogo.model';
import { ICatalogo } from 'app/entities/catalogo/catalogo.model';
import { IDireccion } from 'app/entities/direccion/direccion.model';
import { DireccionService } from 'app/entities/direccion/service/direccion.service';
import { ITipoDireccion } from 'app/entities/tipo-direccion/tipo-direccion.model';
import { TipoDireccionService } from 'app/entities/tipo-direccion/service/tipo-direccion.service';
import { DireccionModalComponent } from 'app/shared/components/direccion-modal/direccion-modal.component';
import { ClienteService } from '../service/cliente.service';
import { ICliente } from '../cliente.model';
import { ClienteFormGroup, ClienteFormService } from './cliente-form.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClienteUpdateComponent implements OnInit {
  readonly fallbackDocumentTypes: DocumentoTipoOption[] = [
    { code: 'C', label: 'Cedula' },
    { code: 'P', label: 'Pasaporte' },
    { code: 'R', label: 'RUC' },
  ];
  readonly fallbackSexoOptions: CatalogOption[] = [
    { code: 'M', label: 'Masculino' },
    { code: 'F', label: 'Femenino' },
  ];
  readonly fallbackEstadoCivilOptions: CatalogOption[] = [
    { code: 'SOL', label: 'Soltero/a' },
    { code: 'CAS', label: 'Casado/a' },
  ];
  readonly fallbackTipoSangreOptions: CatalogOption[] = [
    { code: 'O+', label: 'O+' },
    { code: 'A+', label: 'A+' },
    { code: 'B+', label: 'B+' },
    { code: 'AB+', label: 'AB+' },
  ];

  isSaving = false;
  isUploading = false;
  isLoadingDirecciones = false;
  cliente: ICliente | null = null;
  uploadError: string | null = null;

  provinciasSharedCollection: IProvincia[] = [];
  filteredCiudadsCollection: ICiudad[] = [];
  selectedProvinciaId: number | null = null;
  documentoTipoOptions: DocumentoTipoOption[] = [];
  sexoOptions: CatalogOption[] = [];
  estadoCivilOptions: CatalogOption[] = [];
  tipoSangreOptions: CatalogOption[] = [];
  tipoClienteCatalogOptions: CatalogOption[] = [];
  direcciones: IDireccion[] = [];
  tipoDireccionLabelById = new Map<number, string>();

  protected clienteService = inject(ClienteService);
  protected clienteFormService = inject(ClienteFormService);
  protected ciudadService = inject(CiudadService);
  protected provinciaService = inject(ProvinciaService);
  protected catalogoService = inject(CatalogoService);
  protected tipoCatalogoService = inject(TipoCatalogoService);
  protected direccionService = inject(DireccionService);
  protected tipoDireccionService = inject(TipoDireccionService);
  protected activatedRoute = inject(ActivatedRoute);
  protected activeCompanyService = inject(ActiveCompanyService);
  protected fileService = inject(FileService);
  protected modalService = inject(NgbModal);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClienteFormGroup = this.clienteFormService.createClienteFormGroup();
  private readonly syncNoCiaFromSession = effect(
    () => {
      const sessionNoCia = this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
      if (sessionNoCia === null) {
        return;
      }
      const noCiaControl = this.editForm.get('noCia');
      const currentNoCia = noCiaControl?.value;
      if (currentNoCia === null || currentNoCia === undefined) {
        noCiaControl?.setValue(sessionNoCia);
      }
    },
    { allowSignalWrites: true },
  );

  compareCiudad = (o1: ICiudad | null, o2: ICiudad | null): boolean => this.ciudadService.compareCiudad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.cliente = cliente;
      if (cliente) {
        this.updateForm(cliente);
        this.loadDireccionesByCliente();
      }
      this.ensureNoCiaFromSession();

      this.loadRelationshipsOptions();
      this.loadTipoDireccionLabels();
      this.loadDocumentTypeOptions();
      this.initializeCatalogDrivenFields();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.ensureNoCiaFromSession();
    this.isSaving = true;
    const cliente = this.clienteFormService.getCliente(this.editForm);
    if (cliente.id !== null) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.cliente = cliente;
    this.clienteFormService.resetForm(this.editForm, cliente);
  }

  protected loadRelationshipsOptions(): void {
    this.provinciaService
      .query({ sort: ['descripcion,asc'] })
      .pipe(map((res: HttpResponse<IProvincia[]>) => res.body ?? []))
      .subscribe((provincias: IProvincia[]) => {
        this.provinciasSharedCollection = provincias;
      });

    this.initializeDependentCitySelection();
  }

  onProvinciaChange(value: string | number | null): void {
    const provinciaId = Number(value);
    this.selectedProvinciaId = Number.isFinite(provinciaId) && provinciaId > 0 ? provinciaId : null;
    this.loadCitiesForSelectedProvince();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const selectedFile = input.files?.[0];
    if (!selectedFile) {
      return;
    }

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
          this.editForm.patchValue({ pathImagen: storedFilename });
          this.editForm.get('pathImagen')?.markAsDirty();
          this.editForm.get('pathImagen')?.markAsTouched();
        },
        error: () => {
          this.uploadError = 'No se pudo subir la imagen. Intenta nuevamente.';
        },
      });
  }

  getCurrentImageUrl(): string | null {
    const currentPathImagen = this.editForm.get('pathImagen')?.value;
    return currentPathImagen ? this.fileService.getFileUrl(currentPathImagen) : null;
  }

  openNuevaDireccion(): void {
    const clienteId = this.cliente?.id;
    if (!clienteId) {
      return;
    }
    const modalRef = this.modalService.open(DireccionModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.clienteId = clienteId;
    modalRef.componentInstance.direccion = null;
    modalRef.closed.subscribe(reason => {
      if (reason === 'saved') {
        this.loadDireccionesByCliente();
      }
    });
  }

  openEditarDireccion(direccion: IDireccion): void {
    const clienteId = this.cliente?.id;
    if (!clienteId) {
      return;
    }
    const modalRef = this.modalService.open(DireccionModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.clienteId = clienteId;
    modalRef.componentInstance.direccion = direccion;
    modalRef.closed.subscribe(reason => {
      if (reason === 'saved') {
        this.loadDireccionesByCliente();
      }
    });
  }

  deleteDireccion(direccion: IDireccion): void {
    if (!direccion.id) {
      return;
    }
    const confirmed = window.confirm('Esta seguro que desea eliminar esta direccion?');
    if (!confirmed) {
      return;
    }
    this.direccionService.delete(direccion.id).subscribe({
      next: () => this.loadDireccionesByCliente(),
    });
  }

  getTipoDireccionLabel(direccion: IDireccion): string {
    const tipoId = direccion.tipoDireccion?.id;
    if (!tipoId) {
      return '';
    }
    return this.tipoDireccionLabelById.get(tipoId) ?? `${tipoId}`;
  }

  private loadDocumentTypeOptions(): void {
    this.tipoCatalogoService.query({ sort: ['descripcion,asc'] }).subscribe((res: HttpResponse<ITipoCatalogo[]>) => {
      const tipos = res.body ?? [];
      const tipoDocumento = tipos.find(tipo => this.isTipoDocumentoCatalog(tipo));
      if (!tipoDocumento?.id) {
        this.documentoTipoOptions = this.fallbackDocumentTypes;
        this.ensureTipoDocumentoValue();
        return;
      }

      this.catalogoService
        .query({
          'tipoCatalogoId.equals': tipoDocumento.id,
          sort: ['orden,asc', 'descripcion1,asc'],
        })
        .subscribe((catalogosRes: HttpResponse<ICatalogo[]>) => {
          const fromCatalog = (catalogosRes.body ?? [])
            .map(item => this.toDocumentoTipoOption(item))
            .filter((item): item is DocumentoTipoOption => item !== null);

          this.documentoTipoOptions = fromCatalog.length > 0 ? fromCatalog : this.fallbackDocumentTypes;
          this.ensureTipoDocumentoValue();
        });
    });
  }

  private loadSexoOptions(): void {
    this.loadCatalogOptions(['SEXO'], this.fallbackSexoOptions, options => {
      this.sexoOptions = options;
      this.ensureCatalogControlValue('sexo', options);
    });
  }

  private loadEstadoCivilOptions(): void {
    this.loadCatalogOptions(['ESTADO_CIVIL', 'ESTADO_CIVILIDAD'], this.fallbackEstadoCivilOptions, options => {
      this.estadoCivilOptions = options;
      this.ensureCatalogControlValue('estadoCivil', options);
    });
  }

  private loadTipoSangreOptions(): void {
    this.loadCatalogOptions(['TIPO_SANGRE', 'SANGRE'], this.fallbackTipoSangreOptions, options => {
      this.tipoSangreOptions = options;
      this.ensureCatalogControlValue('tipoSangre', options);
    });
  }

  private loadTipoClienteFromCatalog(): void {
    this.loadCatalogOptions(['TIPO_CLIENTE'], [], options => {
      this.tipoClienteCatalogOptions = options;
      this.ensureCatalogControlValue('tipoCliente', options);
    });
  }

  private loadCatalogOptions(typeKeys: string[], fallback: CatalogOption[], onLoaded: (options: CatalogOption[]) => void): void {
    this.tipoCatalogoService.query({ sort: ['descripcion,asc'] }).subscribe((res: HttpResponse<ITipoCatalogo[]>) => {
      const tipos = res.body ?? [];
      const matchedTipo = tipos.find(tipo => this.matchesCatalogType(tipo, typeKeys));
      if (!matchedTipo?.id) {
        onLoaded(fallback);
        return;
      }

      this.catalogoService
        .query({
          'tipoCatalogoId.equals': matchedTipo.id,
          sort: ['orden,asc', 'descripcion1,asc'],
        })
        .subscribe((catalogosRes: HttpResponse<ICatalogo[]>) => {
          const options = (catalogosRes.body ?? [])
            .map(item => this.toCatalogOption(item))
            .filter((item): item is CatalogOption => item !== null);
          onLoaded(options.length > 0 ? options : fallback);
        });
    });
  }

  private ensureCatalogControlValue(controlName: 'sexo' | 'estadoCivil' | 'tipoSangre' | 'tipoCliente', options: CatalogOption[]): void {
    const control = this.editForm.get(controlName);
    const current = `${control?.value ?? ''}`.trim().toUpperCase();
    if (current) {
      return;
    }
    control?.setValue(options[0]?.code ?? '');
  }

  private toCatalogOption(item: ICatalogo): CatalogOption | null {
    const code = (item.texto1 ?? '').trim().toUpperCase();
    if (!code) {
      return null;
    }
    const description = (item.descripcion1 ?? '').trim();
    return {
      code,
      label: description || code,
    };
  }

  private matchesCatalogType(tipo: ITipoCatalogo, keys: string[]): boolean {
    const normalizedKeys = keys.map(key => this.normalizeCatalogKey(key));
    const values = [tipo.descripcion ?? '', tipo.categoria ?? ''].map(value => this.normalizeCatalogKey(value)).filter(Boolean);
    return values.some(value => normalizedKeys.includes(value));
  }

  private normalizeCatalogKey(value: string): string {
    return value.trim().toUpperCase().replace(/\s+/g, '_').replace(/[-.]/g, '_');
  }

  private ensureTipoDocumentoValue(): void {
    const control = this.editForm.get('tipoDocumento');
    const current = control?.value;
    const exists = this.documentoTipoOptions.some(option => option.code === current);
    if (!exists) {
      control?.setValue(this.documentoTipoOptions[0]?.code ?? 'C');
    }
  }

  private toDocumentoTipoOption(item: ICatalogo): DocumentoTipoOption | null {
    const code = (item.texto1 ?? '').trim().toUpperCase();
    if (!code) {
      return null;
    }
    const description = (item.descripcion1 ?? '').trim();
    return {
      code,
      label: description || code,
    };
  }

  private isTipoDocumentoCatalog(tipo: ITipoCatalogo): boolean {
    const keys = [tipo.descripcion ?? '', tipo.categoria ?? '']
      .map(value => value.trim().toUpperCase().replace(/\s+/g, '_'))
      .filter(Boolean);
    return keys.includes('TIPO_DOCUMENTO') || keys.includes('TIPO_DOC') || keys.includes('DOCUMENTO');
  }

  private initializeCatalogDrivenFields(): void {
    this.loadSexoOptions();
    this.loadEstadoCivilOptions();
    this.loadTipoSangreOptions();
    this.loadTipoClienteFromCatalog();
  }

  private initializeDependentCitySelection(): void {
    const selectedCiudadControlValue = this.editForm.get('ciudad')?.value as Pick<ICiudad, 'id'> | ICiudad | null;
    const selectedCiudadId = selectedCiudadControlValue?.id ?? null;
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
      .query({
        'provinciaId.equals': this.selectedProvinciaId,
        sort: ['descripcion,asc'],
      })
      .pipe(map((res: HttpResponse<ICiudad[]>) => res.body ?? []))
      .subscribe(ciudads => {
        this.filteredCiudadsCollection = ciudads;
        if (selectedCiudadId) {
          const matched = ciudads.find(ciudad => ciudad.id === selectedCiudadId) ?? null;
          this.editForm.get('ciudad')?.setValue(matched, { emitEvent: false });
          return;
        }
        this.editForm.get('ciudad')?.setValue(null);
      });
  }

  private loadDireccionesByCliente(): void {
    const clienteId = this.cliente?.id;
    if (!clienteId) {
      this.direcciones = [];
      return;
    }
    this.isLoadingDirecciones = true;
    this.direccionService
      .query({
        'clienteId.equals': clienteId,
        sort: ['id,asc'],
      })
      .pipe(finalize(() => (this.isLoadingDirecciones = false)))
      .subscribe({
        next: (res: HttpResponse<IDireccion[]>) => {
          this.direcciones = res.body ?? [];
        },
        error: () => {
          this.direcciones = [];
        },
      });
  }

  private loadTipoDireccionLabels(): void {
    this.tipoDireccionService
      .query({ sort: ['descripcion,asc'] })
      .pipe(map((res: HttpResponse<ITipoDireccion[]>) => res.body ?? []))
      .subscribe(tipos => {
        this.tipoDireccionLabelById = new Map(tipos.map(tipo => [tipo.id, tipo.descripcion ?? `${tipo.id}`]));
      });
  }

  private ensureNoCiaFromSession(): void {
    const noCiaControl = this.editForm.get('noCia');
    const currentNoCia = noCiaControl?.value;
    if (currentNoCia !== null && currentNoCia !== undefined) {
      return;
    }
    const sessionNoCia = this.getSessionCompanyNoCia();
    if (sessionNoCia !== null) {
      noCiaControl?.setValue(sessionNoCia);
    }
  }

  private getSessionCompanyNoCia(): number | null {
    return this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
  }
}

interface DocumentoTipoOption {
  code: string;
  label: string;
}

interface CatalogOption {
  code: string;
  label: string;
}
