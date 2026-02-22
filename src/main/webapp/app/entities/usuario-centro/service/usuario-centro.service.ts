import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUsuarioCentro, NewUsuarioCentro } from '../usuario-centro.model';

export type PartialUpdateUsuarioCentro = Partial<IUsuarioCentro> & Pick<IUsuarioCentro, 'id'>;

export type EntityResponseType = HttpResponse<IUsuarioCentro>;
export type EntityArrayResponseType = HttpResponse<IUsuarioCentro[]>;

@Injectable({ providedIn: 'root' })
export class UsuarioCentroService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/usuario-centros');

  create(usuarioCentro: NewUsuarioCentro): Observable<EntityResponseType> {
    return this.http.post<IUsuarioCentro>(this.resourceUrl, usuarioCentro, { observe: 'response' });
  }

  update(usuarioCentro: IUsuarioCentro): Observable<EntityResponseType> {
    return this.http.put<IUsuarioCentro>(`${this.resourceUrl}/${this.getUsuarioCentroIdentifier(usuarioCentro)}`, usuarioCentro, {
      observe: 'response',
    });
  }

  partialUpdate(usuarioCentro: PartialUpdateUsuarioCentro): Observable<EntityResponseType> {
    return this.http.patch<IUsuarioCentro>(`${this.resourceUrl}/${this.getUsuarioCentroIdentifier(usuarioCentro)}`, usuarioCentro, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUsuarioCentro>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUsuarioCentro[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUsuarioCentroIdentifier(usuarioCentro: Pick<IUsuarioCentro, 'id'>): number {
    return usuarioCentro.id;
  }

  compareUsuarioCentro(o1: Pick<IUsuarioCentro, 'id'> | null, o2: Pick<IUsuarioCentro, 'id'> | null): boolean {
    return o1 && o2 ? this.getUsuarioCentroIdentifier(o1) === this.getUsuarioCentroIdentifier(o2) : o1 === o2;
  }

  addUsuarioCentroToCollectionIfMissing<Type extends Pick<IUsuarioCentro, 'id'>>(
    usuarioCentroCollection: Type[],
    ...usuarioCentrosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const usuarioCentros: Type[] = usuarioCentrosToCheck.filter(isPresent);
    if (usuarioCentros.length > 0) {
      const usuarioCentroCollectionIdentifiers = usuarioCentroCollection.map(usuarioCentroItem =>
        this.getUsuarioCentroIdentifier(usuarioCentroItem),
      );
      const usuarioCentrosToAdd = usuarioCentros.filter(usuarioCentroItem => {
        const usuarioCentroIdentifier = this.getUsuarioCentroIdentifier(usuarioCentroItem);
        if (usuarioCentroCollectionIdentifiers.includes(usuarioCentroIdentifier)) {
          return false;
        }
        usuarioCentroCollectionIdentifiers.push(usuarioCentroIdentifier);
        return true;
      });
      return [...usuarioCentrosToAdd, ...usuarioCentroCollection];
    }
    return usuarioCentroCollection;
  }
}
