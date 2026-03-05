import { Component, OnInit, inject, signal } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { ICatalogo } from '../catalogo.model';
import { CatalogoService } from '../service/catalogo.service';
import { ITipoCatalogo } from 'app/entities/tipo-catalogo/tipo-catalogo.model';
import { TipoCatalogoService } from 'app/entities/tipo-catalogo/service/tipo-catalogo.service';

@Component({
  selector: 'jhi-catalogos-panel',
  templateUrl: './catalogos-panel.component.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class CatalogosPanelComponent implements OnInit {
  tipoCatalogos = signal<ITipoCatalogo[]>([]);
  catalogos = signal<ICatalogo[]>([]);
  selectedTipoCatalogoId = signal<number | null>(null);

  isLoadingTipos = false;
  isLoadingCatalogos = false;
  isSavingTipo = false;
  isSavingCatalogo = false;

  editingTipoCatalogoId: number | null = null;
  editingCatalogoId: number | null = null;

  private readonly fb = inject(FormBuilder);
  private readonly tipoCatalogoService = inject(TipoCatalogoService);
  private readonly catalogoService = inject(CatalogoService);
  private readonly activeCompanyService = inject(ActiveCompanyService);

  tipoCatalogoForm = this.fb.group({
    descripcion: ['', [Validators.required]],
    categoria: ['', [Validators.required]],
  });

  catalogoForm = this.fb.group({
    descripcion1: ['', [Validators.required]],
    descripcion2: [''],
    estado: ['A'],
    orden: [0],
    texto1: [''],
    texto2: [''],
  });

  ngOnInit(): void {
    this.loadTipoCatalogos();
  }

  selectTipoCatalogo(tipoCatalogo: ITipoCatalogo): void {
    if (!tipoCatalogo.id) {
      return;
    }
    this.selectedTipoCatalogoId.set(tipoCatalogo.id);
    this.cancelCatalogoEdit();
    this.loadCatalogosByTipo(tipoCatalogo.id);
  }

  saveTipoCatalogo(): void {
    const noCia = this.getSessionCompanyId();
    if (noCia === null) {
      return;
    }
    if (this.tipoCatalogoForm.invalid) {
      this.tipoCatalogoForm.markAllAsTouched();
      return;
    }

    this.isSavingTipo = true;
    const formValue = this.tipoCatalogoForm.getRawValue();
    const request$ =
      this.editingTipoCatalogoId !== null
        ? this.tipoCatalogoService.update({
            id: this.editingTipoCatalogoId,
            noCia,
            descripcion: formValue.descripcion?.trim() ?? null,
            categoria: formValue.categoria?.trim() ?? null,
          })
        : this.tipoCatalogoService.create({
            id: null,
            noCia,
            descripcion: formValue.descripcion?.trim() ?? null,
            categoria: formValue.categoria?.trim() ?? null,
          });

    request$.pipe(finalize(() => (this.isSavingTipo = false))).subscribe((res: HttpResponse<ITipoCatalogo>) => {
      const createdOrUpdated = res.body;
      this.loadTipoCatalogos(createdOrUpdated?.id ?? this.selectedTipoCatalogoId());
      this.cancelTipoCatalogoEdit();
    });
  }

  editTipoCatalogo(tipoCatalogo: ITipoCatalogo): void {
    this.editingTipoCatalogoId = tipoCatalogo.id;
    this.tipoCatalogoForm.patchValue({
      descripcion: tipoCatalogo.descripcion ?? '',
      categoria: tipoCatalogo.categoria ?? '',
    });
  }

  deleteTipoCatalogo(tipoCatalogo: ITipoCatalogo): void {
    if (!tipoCatalogo.id) {
      return;
    }
    if (!window.confirm(`Se eliminará el tipo de catálogo "${tipoCatalogo.descripcion ?? tipoCatalogo.id}". ¿Deseas continuar?`)) {
      return;
    }
    this.tipoCatalogoService.delete(tipoCatalogo.id).subscribe(() => {
      const selectedId = this.selectedTipoCatalogoId();
      this.cancelTipoCatalogoEdit();
      this.loadTipoCatalogos(selectedId === tipoCatalogo.id ? null : selectedId);
    });
  }

  cancelTipoCatalogoEdit(): void {
    this.editingTipoCatalogoId = null;
    this.tipoCatalogoForm.reset({
      descripcion: '',
      categoria: '',
    });
  }

  saveCatalogo(): void {
    const tipoCatalogoId = this.selectedTipoCatalogoId();
    const noCia = this.getSessionCompanyId();
    if (!tipoCatalogoId) {
      return;
    }
    if (noCia === null) {
      return;
    }
    if (this.catalogoForm.invalid) {
      this.catalogoForm.markAllAsTouched();
      return;
    }

    this.isSavingCatalogo = true;
    const formValue = this.catalogoForm.getRawValue();
    const payload = {
      noCia,
      descripcion1: formValue.descripcion1?.trim() ?? null,
      descripcion2: formValue.descripcion2?.trim() ?? null,
      estado: formValue.estado?.trim() ?? null,
      orden: Number(formValue.orden ?? 0),
      texto1: formValue.texto1?.trim() ?? null,
      texto2: formValue.texto2?.trim() ?? null,
      tipoCatalogo: { id: tipoCatalogoId },
    };

    const request$ =
      this.editingCatalogoId !== null
        ? this.catalogoService.update({
            id: this.editingCatalogoId,
            ...payload,
          })
        : this.catalogoService.create({
            id: null,
            ...payload,
          });

    request$.pipe(finalize(() => (this.isSavingCatalogo = false))).subscribe(() => {
      this.loadCatalogosByTipo(tipoCatalogoId);
      this.cancelCatalogoEdit();
    });
  }

  editCatalogo(catalogo: ICatalogo): void {
    this.editingCatalogoId = catalogo.id;
    this.catalogoForm.patchValue({
      descripcion1: catalogo.descripcion1 ?? '',
      descripcion2: catalogo.descripcion2 ?? '',
      estado: catalogo.estado ?? 'A',
      orden: catalogo.orden ?? 0,
      texto1: catalogo.texto1 ?? '',
      texto2: catalogo.texto2 ?? '',
    });
  }

  deleteCatalogo(catalogo: ICatalogo): void {
    if (!catalogo.id) {
      return;
    }
    if (!window.confirm(`Se eliminará el catálogo "${catalogo.descripcion1 ?? catalogo.id}". ¿Deseas continuar?`)) {
      return;
    }
    this.catalogoService.delete(catalogo.id).subscribe(() => {
      const tipoCatalogoId = this.selectedTipoCatalogoId();
      if (tipoCatalogoId) {
        this.loadCatalogosByTipo(tipoCatalogoId);
      }
      this.cancelCatalogoEdit();
    });
  }

  cancelCatalogoEdit(): void {
    this.editingCatalogoId = null;
    this.catalogoForm.reset({
      descripcion1: '',
      descripcion2: '',
      estado: 'A',
      orden: 0,
      texto1: '',
      texto2: '',
    });
  }

  private loadTipoCatalogos(preferredSelectionId?: number | null): void {
    this.isLoadingTipos = true;
    this.tipoCatalogoService
      .query({ sort: ['descripcion,asc'] })
      .pipe(finalize(() => (this.isLoadingTipos = false)))
      .subscribe((res: HttpResponse<ITipoCatalogo[]>) => {
        const tipos = res.body ?? [];
        this.tipoCatalogos.set(tipos);

        if (tipos.length === 0) {
          this.selectedTipoCatalogoId.set(null);
          this.catalogos.set([]);
          return;
        }

        const selectedId = preferredSelectionId ?? this.selectedTipoCatalogoId() ?? tipos[0].id;
        const exists = tipos.some(tipo => tipo.id === selectedId);
        const nextSelectedId = exists ? selectedId : tipos[0].id;
        this.selectedTipoCatalogoId.set(nextSelectedId);
        this.loadCatalogosByTipo(nextSelectedId);
      });
  }

  private loadCatalogosByTipo(tipoCatalogoId: number): void {
    this.isLoadingCatalogos = true;
    this.catalogoService
      .query({
        'tipoCatalogoId.equals': tipoCatalogoId,
        sort: ['orden,asc', 'descripcion1,asc'],
      })
      .pipe(finalize(() => (this.isLoadingCatalogos = false)))
      .subscribe((res: HttpResponse<ICatalogo[]>) => {
        this.catalogos.set(res.body ?? []);
      });
  }

  private getSessionCompanyId(): number | null {
    return this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
  }
}
