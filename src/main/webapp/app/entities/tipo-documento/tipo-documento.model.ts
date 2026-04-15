/** PK compuesta: (noCia, tipoDocumento) — sin surrogate id */
export interface ITipoDocumento {
  noCia: number;
  tipoDocumento: string;
  descripcion?: string | null;
  indice?: string | null;
  codigoSri?: string | null;
}

export type NewTipoDocumento = ITipoDocumento;
