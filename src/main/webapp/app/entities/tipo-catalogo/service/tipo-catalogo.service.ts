import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoCatalogo, NewTipoCatalogo } from '../tipo-catalogo.model';

export type PartialUpdateTipoCatalogo = Partial<ITipoCatalogo> & Pick<ITipoCatalogo, 'id'>;

export type EntityResponseType = HttpResponse<ITipoCatalogo>;
export type EntityArrayResponseType = HttpResponse<ITipoCatalogo[]>;

@Injectable({ providedIn: 'root' })
export class TipoCatalogoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-catalogos');

  create(tipoCatalogo: NewTipoCatalogo): Observable<EntityResponseType> {
    return this.http.post<ITipoCatalogo>(this.resourceUrl, tipoCatalogo, { observe: 'response' });
  }

  update(tipoCatalogo: ITipoCatalogo): Observable<EntityResponseType> {
    return this.http.put<ITipoCatalogo>(`${this.resourceUrl}/${this.getTipoCatalogoIdentifier(tipoCatalogo)}`, tipoCatalogo, {
      observe: 'response',
    });
  }

  partialUpdate(tipoCatalogo: PartialUpdateTipoCatalogo): Observable<EntityResponseType> {
    return this.http.patch<ITipoCatalogo>(`${this.resourceUrl}/${this.getTipoCatalogoIdentifier(tipoCatalogo)}`, tipoCatalogo, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoCatalogo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoCatalogo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTipoCatalogoIdentifier(tipoCatalogo: Pick<ITipoCatalogo, 'id'>): number {
    return tipoCatalogo.id;
  }

  compareTipoCatalogo(o1: Pick<ITipoCatalogo, 'id'> | null, o2: Pick<ITipoCatalogo, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoCatalogoIdentifier(o1) === this.getTipoCatalogoIdentifier(o2) : o1 === o2;
  }

  addTipoCatalogoToCollectionIfMissing<Type extends Pick<ITipoCatalogo, 'id'>>(
    tipoCatalogoCollection: Type[],
    ...tipoCatalogosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoCatalogos: Type[] = tipoCatalogosToCheck.filter(isPresent);
    if (tipoCatalogos.length > 0) {
      const tipoCatalogoCollectionIdentifiers = tipoCatalogoCollection.map(tipoCatalogoItem =>
        this.getTipoCatalogoIdentifier(tipoCatalogoItem),
      );
      const tipoCatalogosToAdd = tipoCatalogos.filter(tipoCatalogoItem => {
        const tipoCatalogoIdentifier = this.getTipoCatalogoIdentifier(tipoCatalogoItem);
        if (tipoCatalogoCollectionIdentifiers.includes(tipoCatalogoIdentifier)) {
          return false;
        }
        tipoCatalogoCollectionIdentifiers.push(tipoCatalogoIdentifier);
        return true;
      });
      return [...tipoCatalogosToAdd, ...tipoCatalogoCollection];
    }
    return tipoCatalogoCollection;
  }
}
