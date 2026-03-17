import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions, DateSelectArg, DatesSetArg, EventClickArg, EventDropArg } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { EventResizeDoneArg } from '@fullcalendar/interaction';
import timeGridPlugin from '@fullcalendar/timegrid';
import { finalize } from 'rxjs';

import SharedModule from 'app/shared/shared.module';
import { ICalendarioCita, IGoogleCalendarStatus } from './calendario.model';
import { CalendarioService } from './service/calendario.service';
import { CitaModalComponent, ICitaModalResult } from 'app/shared/components/cita-modal/cita-modal.component';

@Component({
  standalone: true,
  selector: 'jhi-calendario',
  templateUrl: './calendario.component.html',
  imports: [SharedModule, FormsModule, FullCalendarModule],
})
export class CalendarioComponent implements OnInit {
  protected readonly calendarioService = inject(CalendarioService);
  protected modalService = inject(NgbModal);

  isLoading = signal(false);
  googleStatus = signal<IGoogleCalendarStatus>({ connected: false });
  lastFrom = signal<string | null>(null);
  lastTo = signal<string | null>(null);
  private citasById = new Map<number, ICalendarioCita>();

  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay',
    },
    locale: 'es',
    selectable: true,
    editable: true,
    nowIndicator: true,
    select: (arg: DateSelectArg) => this.onSelect(arg),
    datesSet: (arg: DatesSetArg) => this.onDatesSet(arg),
    eventClick: (arg: EventClickArg) => this.onEventClick(arg),
    eventDrop: (arg: EventDropArg) => this.onEventMoveOrResize(arg.event.id, arg.event.title, arg.event.start, arg.event.end),
    eventResize: (arg: EventResizeDoneArg) => this.onEventMoveOrResize(arg.event.id, arg.event.title, arg.event.start, arg.event.end),
    events: [],
  };

  ngOnInit(): void {
    this.loadGoogleStatus();
  }

  conectarGoogle(): void {
    this.calendarioService.getGoogleAuthUrl().subscribe({
      next: ({ url }) => {
        window.open(url, '_blank', 'width=620,height=800');
      },
    });
  }

  sincronizarGoogle(): void {
    const from = this.lastFrom();
    const to = this.lastTo();
    if (!from || !to) {
      return;
    }

    this.isLoading.set(true);
    this.calendarioService
      .syncGoogle(from, to)
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: () => this.loadCurrentRange(),
      });
  }

  private onDatesSet(arg: DatesSetArg): void {
    this.lastFrom.set(arg.start.toISOString());
    this.lastTo.set(arg.end.toISOString());
    this.loadCurrentRange();
  }

  private onSelect(arg: DateSelectArg): void {
    if (!arg.start) {
      return;
    }

    const modalRef = this.modalService.open(CitaModalComponent, { size: 'lg', backdrop: 'static' });
    const start = arg.allDay ? this.withTime(arg.start, 9, 0) : arg.start;
    const end = arg.allDay ? this.withTime(arg.start, 10, 0) : (arg.end ?? new Date(arg.start.getTime() + 60 * 60 * 1000));

    modalRef.componentInstance.setInitialDates(start, end);

    modalRef.closed.subscribe((result: ICitaModalResult) => {
      if (result.action !== 'save' || !result.payload) {
        return;
      }
      this.isLoading.set(true);
      this.calendarioService
        .create({
          titulo: result.payload.titulo,
          descripcion: result.payload.descripcion,
          inicio: result.payload.inicioIso,
          fin: result.payload.finIso,
          estado: 'PROGRAMADA',
          clienteId: result.payload.clienteId,
        })
        .pipe(finalize(() => this.isLoading.set(false)))
        .subscribe({
          next: () => this.loadCurrentRange(),
        });
    });
  }

  private onEventClick(arg: EventClickArg): void {
    const id = Number(arg.event.id);
    if (!id) {
      return;
    }
    const cita = this.citasById.get(id);
    if (!cita) {
      return;
    }

    const modalRef = this.modalService.open(CitaModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.existingCita = cita;

    modalRef.closed.subscribe((result: ICitaModalResult) => {
      if (result.action === 'cancel-cita') {
        this.isLoading.set(true);
        this.calendarioService
          .cancel(id)
          .pipe(finalize(() => this.isLoading.set(false)))
          .subscribe({
            next: () => this.loadCurrentRange(),
          });
        return;
      }

      if (result.action === 'delete-cita') {
        this.isLoading.set(true);
        this.calendarioService
          .delete(id)
          .pipe(finalize(() => this.isLoading.set(false)))
          .subscribe({
            next: () => this.loadCurrentRange(),
          });
        return;
      }

      if (result.action === 'save' && result.payload) {
        this.isLoading.set(true);
        this.calendarioService
          .update({
            id,
            titulo: result.payload.titulo,
            descripcion: result.payload.descripcion,
            inicio: result.payload.inicioIso,
            fin: result.payload.finIso,
            clienteId: result.payload.clienteId,
          })
          .pipe(finalize(() => this.isLoading.set(false)))
          .subscribe({
            next: () => this.loadCurrentRange(),
          });
      }
    });
  }

  private onEventMoveOrResize(idRaw: string, title: string, start: Date | null, end: Date | null): void {
    const id = Number(idRaw);
    if (!id || !start || !end) {
      return;
    }

    this.isLoading.set(true);
    this.calendarioService
      .update({
        id,
        titulo: title,
        inicio: this.toApiLocalDateTime(start),
        fin: this.toApiLocalDateTime(end),
      })
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: () => this.loadCurrentRange(),
      });
  }

  private loadCurrentRange(): void {
    const from = this.lastFrom();
    const to = this.lastTo();
    if (!from || !to) {
      return;
    }

    this.isLoading.set(true);
    this.calendarioService
      .getByRange(from, to)
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: citas => this.renderEvents(citas),
      });
  }

  private renderEvents(citas: ICalendarioCita[]): void {
    this.citasById = new Map(citas.map(cita => [cita.id, cita]));
    this.calendarOptions = {
      ...this.calendarOptions,
      events: citas.map(cita => ({
        id: String(cita.id),
        title: cita.titulo ?? 'Cita',
        start: cita.inicio ?? undefined,
        end: cita.fin ?? undefined,
        allDay: false,
        backgroundColor: cita.estado === 'CANCELADA' ? '#dc3545' : '#0d6efd',
        borderColor: cita.estado === 'CANCELADA' ? '#dc3545' : '#0d6efd',
      })),
    };
  }

  private withTime(value: Date, hour: number, minute: number): Date {
    const date = new Date(value);
    date.setHours(hour, minute, 0, 0);
    return date;
  }

  private toApiLocalDateTime(value: Date): string {
    const pad = (n: number): string => String(n).padStart(2, '0');
    return `${value.getFullYear()}-${pad(value.getMonth() + 1)}-${pad(value.getDate())}T${pad(value.getHours())}:${pad(value.getMinutes())}:00`;
  }

  private loadGoogleStatus(): void {
    this.calendarioService.getGoogleStatus().subscribe({
      next: status => this.googleStatus.set(status),
    });
  }
}
