import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMarca, NewMarca } from '../marca.model';

export type PartialUpdateMarca = Partial<IMarca> & Pick<IMarca, 'id'>;

export type EntityResponseType = HttpResponse<IMarca>;
export type EntityArrayResponseType = HttpResponse<IMarca[]>;

@Injectable({ providedIn: 'root' })
export class MarcaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/marcas');

  create(marca: NewMarca): Observable<EntityResponseType> {
    return this.http.post<IMarca>(this.resourceUrl, marca, { observe: 'response' });
  }

  update(marca: IMarca): Observable<EntityResponseType> {
    return this.http.put<IMarca>(`${this.resourceUrl}/${this.getMarcaIdentifier(marca)}`, marca, { observe: 'response' });
  }

  partialUpdate(marca: PartialUpdateMarca): Observable<EntityResponseType> {
    return this.http.patch<IMarca>(`${this.resourceUrl}/${this.getMarcaIdentifier(marca)}`, marca, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMarca>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMarca[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMarcaIdentifier(marca: Pick<IMarca, 'id'>): number {
    return marca.id;
  }

  compareMarca(o1: Pick<IMarca, 'id'> | null, o2: Pick<IMarca, 'id'> | null): boolean {
    return o1 && o2 ? this.getMarcaIdentifier(o1) === this.getMarcaIdentifier(o2) : o1 === o2;
  }

  addMarcaToCollectionIfMissing<Type extends Pick<IMarca, 'id'>>(
    marcaCollection: Type[],
    ...marcasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const marcas: Type[] = marcasToCheck.filter(isPresent);
    if (marcas.length > 0) {
      const marcaCollectionIdentifiers = marcaCollection.map(marcaItem => this.getMarcaIdentifier(marcaItem));
      const marcasToAdd = marcas.filter(marcaItem => {
        const marcaIdentifier = this.getMarcaIdentifier(marcaItem);
        if (marcaCollectionIdentifiers.includes(marcaIdentifier)) {
          return false;
        }
        marcaCollectionIdentifiers.push(marcaIdentifier);
        return true;
      });
      return [...marcasToAdd, ...marcaCollection];
    }
    return marcaCollection;
  }
}
