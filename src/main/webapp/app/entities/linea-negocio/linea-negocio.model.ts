/** PK compuesta: (noCia, lineaNo) — sin surrogate id */
export interface ILineaNegocio {
  noCia: number;
  lineaNo: string;
  descripcion?: string | null;
}

export type NewLineaNegocio = ILineaNegocio;
