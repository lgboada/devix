import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoDireccion, NewTipoDireccion } from '../tipo-direccion.model';

export type PartialUpdateTipoDireccion = Partial<ITipoDireccion> & Pick<ITipoDireccion, 'id'>;

export type EntityResponseType = HttpResponse<ITipoDireccion>;
export type EntityArrayResponseType = HttpResponse<ITipoDireccion[]>;

@Injectable({ providedIn: 'root' })
export class TipoDireccionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-direccions');

  create(tipoDireccion: NewTipoDireccion): Observable<EntityResponseType> {
    return this.http.post<ITipoDireccion>(this.resourceUrl, tipoDireccion, { observe: 'response' });
  }

  update(tipoDireccion: ITipoDireccion): Observable<EntityResponseType> {
    return this.http.put<ITipoDireccion>(`${this.resourceUrl}/${this.getTipoDireccionIdentifier(tipoDireccion)}`, tipoDireccion, {
      observe: 'response',
    });
  }

  partialUpdate(tipoDireccion: PartialUpdateTipoDireccion): Observable<EntityResponseType> {
    return this.http.patch<ITipoDireccion>(`${this.resourceUrl}/${this.getTipoDireccionIdentifier(tipoDireccion)}`, tipoDireccion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoDireccion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoDireccion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTipoDireccionIdentifier(tipoDireccion: Pick<ITipoDireccion, 'id'>): number {
    return tipoDireccion.id;
  }

  compareTipoDireccion(o1: Pick<ITipoDireccion, 'id'> | null, o2: Pick<ITipoDireccion, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoDireccionIdentifier(o1) === this.getTipoDireccionIdentifier(o2) : o1 === o2;
  }

  addTipoDireccionToCollectionIfMissing<Type extends Pick<ITipoDireccion, 'id'>>(
    tipoDireccionCollection: Type[],
    ...tipoDireccionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoDireccions: Type[] = tipoDireccionsToCheck.filter(isPresent);
    if (tipoDireccions.length > 0) {
      const tipoDireccionCollectionIdentifiers = tipoDireccionCollection.map(tipoDireccionItem =>
        this.getTipoDireccionIdentifier(tipoDireccionItem),
      );
      const tipoDireccionsToAdd = tipoDireccions.filter(tipoDireccionItem => {
        const tipoDireccionIdentifier = this.getTipoDireccionIdentifier(tipoDireccionItem);
        if (tipoDireccionCollectionIdentifiers.includes(tipoDireccionIdentifier)) {
          return false;
        }
        tipoDireccionCollectionIdentifiers.push(tipoDireccionIdentifier);
        return true;
      });
      return [...tipoDireccionsToAdd, ...tipoDireccionCollection];
    }
    return tipoDireccionCollection;
  }
}
