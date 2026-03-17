import { Component, Input, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ICalendarioCita } from 'app/entities/calendario/calendario.model';

export interface ICitaModalResult {
  action: 'save' | 'cancel-cita' | 'delete-cita';
  payload?: {
    titulo: string;
    descripcion: string;
    inicioIso: string;
    finIso: string;
    clienteId: number;
  };
}

@Component({
  standalone: true,
  selector: 'jhi-cita-modal',
  templateUrl: './cita-modal.component.html',
  imports: [SharedModule, FormsModule],
})
export class CitaModalComponent implements OnInit {
  @Input() existingCita: ICalendarioCita | null = null;

  protected readonly activeModal = inject(NgbActiveModal);
  protected readonly clienteService = inject(ClienteService);

  titulo = '';
  descripcion = '';
  inicioLocal = '';
  finLocal = '';
  clienteId: number | null = null;
  clienteSearch = '';

  isSaving = false;
  isLoadingClientes = false;
  errorMsg: string | null = null;

  clientes: ICliente[] = [];

  ngOnInit(): void {
    if (this.existingCita) {
      this.titulo = this.existingCita.titulo ?? '';
      this.descripcion = this.existingCita.descripcion ?? '';
      this.clienteId = this.existingCita.clienteId ?? null;
      if (this.existingCita.inicio) {
        this.inicioLocal = this.toDatetimeLocal(new Date(this.existingCita.inicio));
      }
      if (this.existingCita.fin) {
        this.finLocal = this.toDatetimeLocal(new Date(this.existingCita.fin));
      }
    }
    this.loadClientes();
  }

  setInitialDates(start: Date, end: Date): void {
    this.inicioLocal = this.toDatetimeLocal(start);
    this.finLocal = this.toDatetimeLocal(end);
  }

  buscarClientes(): void {
    this.loadClientes();
  }

  guardar(): void {
    this.errorMsg = null;
    if (!this.titulo.trim()) {
      this.errorMsg = 'El titulo es obligatorio.';
      return;
    }
    if (!this.inicioLocal || !this.finLocal) {
      this.errorMsg = 'Debe seleccionar fecha y hora de inicio/fin.';
      return;
    }
    if (!this.clienteId) {
      this.errorMsg = 'Debe seleccionar un cliente.';
      return;
    }

    const inicioDate = new Date(this.inicioLocal);
    const finDate = new Date(this.finLocal);
    if (!(finDate > inicioDate)) {
      this.errorMsg = 'La hora fin debe ser mayor a la hora inicio.';
      return;
    }

    const result: ICitaModalResult = {
      action: 'save',
      payload: {
        titulo: this.titulo.trim(),
        descripcion: this.descripcion.trim(),
        inicioIso: this.toApiLocalDateTime(inicioDate),
        finIso: this.toApiLocalDateTime(finDate),
        clienteId: this.clienteId,
      },
    };
    this.activeModal.close(result);
  }

  cancelarCita(): void {
    this.activeModal.close({ action: 'cancel-cita' } satisfies ICitaModalResult);
  }

  eliminarCita(): void {
    this.activeModal.close({ action: 'delete-cita' } satisfies ICitaModalResult);
  }

  cancelar(): void {
    this.activeModal.dismiss('cancel');
  }

  private loadClientes(): void {
    this.isLoadingClientes = true;
    this.clienteService
      .query({
        size: 200,
        sort: ['nombres,asc'],
        ...(this.clienteSearch.trim() ? { 'search.contains': this.clienteSearch.trim() } : {}),
      })
      .pipe(
        map((res: HttpResponse<ICliente[]>) => res.body ?? []),
        finalize(() => (this.isLoadingClientes = false)),
      )
      .subscribe({
        next: clientes => (this.clientes = clientes),
        error: () => {
          this.clientes = [];
          this.errorMsg = 'No se pudo cargar la lista de clientes.';
        },
      });
  }

  private toDatetimeLocal(value: Date): string {
    const pad = (n: number): string => String(n).padStart(2, '0');
    return `${value.getFullYear()}-${pad(value.getMonth() + 1)}-${pad(value.getDate())}T${pad(value.getHours())}:${pad(value.getMinutes())}`;
  }

  private toApiLocalDateTime(value: Date): string {
    const pad = (n: number): string => String(n).padStart(2, '0');
    return `${value.getFullYear()}-${pad(value.getMonth() + 1)}-${pad(value.getDate())}T${pad(value.getHours())}:${pad(value.getMinutes())}:00`;
  }

  isEditMode(): boolean {
    return this.existingCita?.id != null;
  }
}
