export interface ActiveCompany {
  noCia: number;
  principal: boolean;
  /** Nombre de la compania (tabla compania.nombre) desde GET /api/account/companies */
  label?: string | null;
}
