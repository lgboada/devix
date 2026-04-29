export interface ActiveBodega {
  bodegaId: number;
  principal: boolean;
  /** codigo de la bodega */
  codigo?: string | null;
  /** nombre de la bodega */
  label?: string | null;
}
