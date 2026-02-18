import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoProducto, NewTipoProducto } from '../tipo-producto.model';

export type PartialUpdateTipoProducto = Partial<ITipoProducto> & Pick<ITipoProducto, 'id'>;

export type EntityResponseType = HttpResponse<ITipoProducto>;
export type EntityArrayResponseType = HttpResponse<ITipoProducto[]>;

@Injectable({ providedIn: 'root' })
export class TipoProductoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-productos');

  create(tipoProducto: NewTipoProducto): Observable<EntityResponseType> {
    return this.http.post<ITipoProducto>(this.resourceUrl, tipoProducto, { observe: 'response' });
  }

  update(tipoProducto: ITipoProducto): Observable<EntityResponseType> {
    return this.http.put<ITipoProducto>(`${this.resourceUrl}/${this.getTipoProductoIdentifier(tipoProducto)}`, tipoProducto, {
      observe: 'response',
    });
  }

  partialUpdate(tipoProducto: PartialUpdateTipoProducto): Observable<EntityResponseType> {
    return this.http.patch<ITipoProducto>(`${this.resourceUrl}/${this.getTipoProductoIdentifier(tipoProducto)}`, tipoProducto, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoProducto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoProducto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTipoProductoIdentifier(tipoProducto: Pick<ITipoProducto, 'id'>): number {
    return tipoProducto.id;
  }

  compareTipoProducto(o1: Pick<ITipoProducto, 'id'> | null, o2: Pick<ITipoProducto, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoProductoIdentifier(o1) === this.getTipoProductoIdentifier(o2) : o1 === o2;
  }

  addTipoProductoToCollectionIfMissing<Type extends Pick<ITipoProducto, 'id'>>(
    tipoProductoCollection: Type[],
    ...tipoProductosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoProductos: Type[] = tipoProductosToCheck.filter(isPresent);
    if (tipoProductos.length > 0) {
      const tipoProductoCollectionIdentifiers = tipoProductoCollection.map(tipoProductoItem =>
        this.getTipoProductoIdentifier(tipoProductoItem),
      );
      const tipoProductosToAdd = tipoProductos.filter(tipoProductoItem => {
        const tipoProductoIdentifier = this.getTipoProductoIdentifier(tipoProductoItem);
        if (tipoProductoCollectionIdentifiers.includes(tipoProductoIdentifier)) {
          return false;
        }
        tipoProductoCollectionIdentifiers.push(tipoProductoIdentifier);
        return true;
      });
      return [...tipoProductosToAdd, ...tipoProductoCollection];
    }
    return tipoProductoCollection;
  }
}
