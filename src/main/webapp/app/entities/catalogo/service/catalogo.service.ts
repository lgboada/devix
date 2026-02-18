import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogo, NewCatalogo } from '../catalogo.model';

export type PartialUpdateCatalogo = Partial<ICatalogo> & Pick<ICatalogo, 'id'>;

export type EntityResponseType = HttpResponse<ICatalogo>;
export type EntityArrayResponseType = HttpResponse<ICatalogo[]>;

@Injectable({ providedIn: 'root' })
export class CatalogoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalogos');

  create(catalogo: NewCatalogo): Observable<EntityResponseType> {
    return this.http.post<ICatalogo>(this.resourceUrl, catalogo, { observe: 'response' });
  }

  update(catalogo: ICatalogo): Observable<EntityResponseType> {
    return this.http.put<ICatalogo>(`${this.resourceUrl}/${this.getCatalogoIdentifier(catalogo)}`, catalogo, { observe: 'response' });
  }

  partialUpdate(catalogo: PartialUpdateCatalogo): Observable<EntityResponseType> {
    return this.http.patch<ICatalogo>(`${this.resourceUrl}/${this.getCatalogoIdentifier(catalogo)}`, catalogo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICatalogo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCatalogoIdentifier(catalogo: Pick<ICatalogo, 'id'>): number {
    return catalogo.id;
  }

  compareCatalogo(o1: Pick<ICatalogo, 'id'> | null, o2: Pick<ICatalogo, 'id'> | null): boolean {
    return o1 && o2 ? this.getCatalogoIdentifier(o1) === this.getCatalogoIdentifier(o2) : o1 === o2;
  }

  addCatalogoToCollectionIfMissing<Type extends Pick<ICatalogo, 'id'>>(
    catalogoCollection: Type[],
    ...catalogosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const catalogos: Type[] = catalogosToCheck.filter(isPresent);
    if (catalogos.length > 0) {
      const catalogoCollectionIdentifiers = catalogoCollection.map(catalogoItem => this.getCatalogoIdentifier(catalogoItem));
      const catalogosToAdd = catalogos.filter(catalogoItem => {
        const catalogoIdentifier = this.getCatalogoIdentifier(catalogoItem);
        if (catalogoCollectionIdentifiers.includes(catalogoIdentifier)) {
          return false;
        }
        catalogoCollectionIdentifiers.push(catalogoIdentifier);
        return true;
      });
      return [...catalogosToAdd, ...catalogoCollection];
    }
    return catalogoCollection;
  }
}
