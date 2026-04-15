export interface ICompania {
  id: number;
  noCia?: number | null;
  dni?: string | null;
  nombre?: string | null;
  direccion?: string | null;
  email?: string | null;
  telefono?: string | null;
  pathImage?: string | null;
  activa?: boolean | null;
  /** Código establecimiento SRI (3 caracteres). */
  establecimiento?: string | null;
  /** Número contribuyente especial (hasta 10 caracteres). */
  contribuyenteEspecial?: string | null;
  obligadoContabilidad?: boolean | null;
  /** 1 = pruebas SRI, 2 = producción. */
  ambienteSri?: number | null;
  /** Nombre de archivo del certificado .p12 almacenado en el servidor (vía api/files). */
  pathCertificado?: string | null;
  claveCertificado?: string | null;
  /** Indica si existe una clave configurada (sin exponerla). */
  claveCertificadoConfigurada?: boolean | null;
  /** Directorio raíz externo para archivos de la compañía (servidor). */
  pathFileServer?: string | null;
}

export type NewCompania = Omit<ICompania, 'id'> & { id: null };
