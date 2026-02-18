import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoEvento, NewTipoEvento } from '../tipo-evento.model';

export type PartialUpdateTipoEvento = Partial<ITipoEvento> & Pick<ITipoEvento, 'id'>;

export type EntityResponseType = HttpResponse<ITipoEvento>;
export type EntityArrayResponseType = HttpResponse<ITipoEvento[]>;

@Injectable({ providedIn: 'root' })
export class TipoEventoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-eventos');

  create(tipoEvento: NewTipoEvento): Observable<EntityResponseType> {
    return this.http.post<ITipoEvento>(this.resourceUrl, tipoEvento, { observe: 'response' });
  }

  update(tipoEvento: ITipoEvento): Observable<EntityResponseType> {
    return this.http.put<ITipoEvento>(`${this.resourceUrl}/${this.getTipoEventoIdentifier(tipoEvento)}`, tipoEvento, {
      observe: 'response',
    });
  }

  partialUpdate(tipoEvento: PartialUpdateTipoEvento): Observable<EntityResponseType> {
    return this.http.patch<ITipoEvento>(`${this.resourceUrl}/${this.getTipoEventoIdentifier(tipoEvento)}`, tipoEvento, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoEvento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoEvento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTipoEventoIdentifier(tipoEvento: Pick<ITipoEvento, 'id'>): number {
    return tipoEvento.id;
  }

  compareTipoEvento(o1: Pick<ITipoEvento, 'id'> | null, o2: Pick<ITipoEvento, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoEventoIdentifier(o1) === this.getTipoEventoIdentifier(o2) : o1 === o2;
  }

  addTipoEventoToCollectionIfMissing<Type extends Pick<ITipoEvento, 'id'>>(
    tipoEventoCollection: Type[],
    ...tipoEventosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoEventos: Type[] = tipoEventosToCheck.filter(isPresent);
    if (tipoEventos.length > 0) {
      const tipoEventoCollectionIdentifiers = tipoEventoCollection.map(tipoEventoItem => this.getTipoEventoIdentifier(tipoEventoItem));
      const tipoEventosToAdd = tipoEventos.filter(tipoEventoItem => {
        const tipoEventoIdentifier = this.getTipoEventoIdentifier(tipoEventoItem);
        if (tipoEventoCollectionIdentifiers.includes(tipoEventoIdentifier)) {
          return false;
        }
        tipoEventoCollectionIdentifiers.push(tipoEventoIdentifier);
        return true;
      });
      return [...tipoEventosToAdd, ...tipoEventoCollection];
    }
    return tipoEventoCollection;
  }
}
