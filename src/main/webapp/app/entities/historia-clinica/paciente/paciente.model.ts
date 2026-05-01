import dayjs from 'dayjs/esm';
import { ICategoriaPaciente } from '../categoria-paciente/categoria-paciente.model';

export interface IPaciente {
  id: number;
  noCia?: number | null;
  dni?: string | null;
  tipoDni?: string | null;
  nombres?: string | null;
  apellidos?: string | null;
  titulo?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  sexo?: string | null;
  orientacionGenero?: string | null;
  identidadSexual?: string | null;
  grupoSanguineo?: string | null;
  estadoCivil?: string | null;
  nivelEstudio?: string | null;
  ocupacion?: string | null;
  religion?: string | null;
  tipoDiscapacidad?: string | null;
  porcentajeDiscapacidad?: string | null;
  lateralidad?: string | null;
  foto?: string | null;
  numeroHistoria?: string | null;
  tipoHistoria?: string | null;
  prioritario?: string | null;
  comentario?: string | null;
  actividades?: string | null;
  agencia?: string | null;
  area?: string | null;
  ciiu?: string | null;
  fechaIngreso?: dayjs.Dayjs | null;
  fechaEgreso?: dayjs.Dayjs | null;
  motivoSalida?: string | null;
  activo?: boolean | null;
  estado?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  ciudad?: Pick<{ id: number }, 'id'> | null;
  categoriaPaciente?: Pick<ICategoriaPaciente, 'id' | 'nombre'> | null;
}

export type NewPaciente = Omit<IPaciente, 'id'> & { id: null };
