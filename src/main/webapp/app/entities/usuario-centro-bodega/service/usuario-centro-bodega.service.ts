import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUsuarioCentroBodega, NewUsuarioCentroBodega } from '../usuario-centro-bodega.model';

export type PartialUpdateUCB = Partial<IUsuarioCentroBodega> & Pick<IUsuarioCentroBodega, 'id'>;
export type EntityResponseType = HttpResponse<IUsuarioCentroBodega>;
export type EntityArrayResponseType = HttpResponse<IUsuarioCentroBodega[]>;

@Injectable({ providedIn: 'root' })
export class UsuarioCentroBodegaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/usuario-centro-bodegas');

  create(ucb: NewUsuarioCentroBodega): Observable<EntityResponseType> {
    return this.http.post<IUsuarioCentroBodega>(this.resourceUrl, ucb, { observe: 'response' });
  }

  update(ucb: IUsuarioCentroBodega): Observable<EntityResponseType> {
    return this.http.put<IUsuarioCentroBodega>(`${this.resourceUrl}/${this.getIdentifier(ucb)}`, ucb, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUsuarioCentroBodega>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUsuarioCentroBodega[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIdentifier(ucb: Pick<IUsuarioCentroBodega, 'id'>): number {
    return ucb.id;
  }

  compareUCB(o1: Pick<IUsuarioCentroBodega, 'id'> | null, o2: Pick<IUsuarioCentroBodega, 'id'> | null): boolean {
    return o1 && o2 ? this.getIdentifier(o1) === this.getIdentifier(o2) : o1 === o2;
  }

  addToCollectionIfMissing<Type extends Pick<IUsuarioCentroBodega, 'id'>>(
    collection: Type[],
    ...toCheck: (Type | null | undefined)[]
  ): Type[] {
    const items: Type[] = toCheck.filter(isPresent);
    if (items.length > 0) {
      const ids = collection.map(e => this.getIdentifier(e));
      const toAdd = items.filter(e => {
        const eid = this.getIdentifier(e);
        if (ids.includes(eid)) return false;
        ids.push(eid);
        return true;
      });
      return [...toAdd, ...collection];
    }
    return collection;
  }
}
