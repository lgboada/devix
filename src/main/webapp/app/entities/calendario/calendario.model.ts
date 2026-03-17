export interface ICalendarioCita {
  id: number;
  noCia?: number | null;
  titulo?: string | null;
  descripcion?: string | null;
  inicio?: string | null;
  fin?: string | null;
  estado?: string | null;
  googleSynced?: boolean | null;
  googleEventId?: string | null;
  googleCalendarId?: string | null;
  clienteId?: number | null;
  usuarioLogin?: string | null;
}

export type NewCalendarioCita = Omit<ICalendarioCita, 'id'> & { id: null };

export interface IGoogleCalendarStatus {
  connected: boolean;
  googleEmail?: string | null;
}
