import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBodega, NewBodega } from '../bodega.model';

export type PartialUpdateBodega = Partial<IBodega> & Pick<IBodega, 'id'>;

export type EntityResponseType = HttpResponse<IBodega>;
export type EntityArrayResponseType = HttpResponse<IBodega[]>;

@Injectable({ providedIn: 'root' })
export class BodegaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bodegas');

  create(bodega: NewBodega): Observable<EntityResponseType> {
    return this.http.post<IBodega>(this.resourceUrl, bodega, { observe: 'response' });
  }

  update(bodega: IBodega): Observable<EntityResponseType> {
    return this.http.put<IBodega>(`${this.resourceUrl}/${this.getBodegaIdentifier(bodega)}`, bodega, { observe: 'response' });
  }

  partialUpdate(bodega: PartialUpdateBodega): Observable<EntityResponseType> {
    return this.http.patch<IBodega>(`${this.resourceUrl}/${this.getBodegaIdentifier(bodega)}`, bodega, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBodega>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBodega[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBodegaIdentifier(bodega: Pick<IBodega, 'id'>): number {
    return bodega.id;
  }

  compareBodega(o1: Pick<IBodega, 'id'> | null, o2: Pick<IBodega, 'id'> | null): boolean {
    return o1 && o2 ? this.getBodegaIdentifier(o1) === this.getBodegaIdentifier(o2) : o1 === o2;
  }

  addBodegaToCollectionIfMissing<Type extends Pick<IBodega, 'id'>>(
    bodegaCollection: Type[],
    ...bodegasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bodegas: Type[] = bodegasToCheck.filter(isPresent);
    if (bodegas.length > 0) {
      const collectionIds = bodegaCollection.map(b => this.getBodegaIdentifier(b));
      const toAdd = bodegas.filter(b => {
        const bid = this.getBodegaIdentifier(b);
        if (collectionIds.includes(bid)) {
          return false;
        }
        collectionIds.push(bid);
        return true;
      });
      return [...toAdd, ...bodegaCollection];
    }
    return bodegaCollection;
  }
}
