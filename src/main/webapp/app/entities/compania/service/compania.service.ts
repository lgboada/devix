import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompania, NewCompania } from '../compania.model';

export type PartialUpdateCompania = Partial<ICompania> & Pick<ICompania, 'id'>;

export type EntityResponseType = HttpResponse<ICompania>;
export type EntityArrayResponseType = HttpResponse<ICompania[]>;

@Injectable({ providedIn: 'root' })
export class CompaniaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/companias');

  create(compania: NewCompania): Observable<EntityResponseType> {
    return this.http.post<ICompania>(this.resourceUrl, compania, { observe: 'response' });
  }

  update(compania: ICompania): Observable<EntityResponseType> {
    return this.http.put<ICompania>(`${this.resourceUrl}/${this.getCompaniaIdentifier(compania)}`, compania, { observe: 'response' });
  }

  partialUpdate(compania: PartialUpdateCompania): Observable<EntityResponseType> {
    return this.http.patch<ICompania>(`${this.resourceUrl}/${this.getCompaniaIdentifier(compania)}`, compania, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompania>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompania[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompaniaIdentifier(compania: Pick<ICompania, 'id'>): number {
    return compania.id;
  }

  compareCompania(o1: Pick<ICompania, 'id'> | null, o2: Pick<ICompania, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompaniaIdentifier(o1) === this.getCompaniaIdentifier(o2) : o1 === o2;
  }

  addCompaniaToCollectionIfMissing<Type extends Pick<ICompania, 'id'>>(
    companiaCollection: Type[],
    ...companiasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const companias: Type[] = companiasToCheck.filter(isPresent);
    if (companias.length > 0) {
      const companiaCollectionIdentifiers = companiaCollection.map(companiaItem => this.getCompaniaIdentifier(companiaItem));
      const companiasToAdd = companias.filter(companiaItem => {
        const companiaIdentifier = this.getCompaniaIdentifier(companiaItem);
        if (companiaCollectionIdentifiers.includes(companiaIdentifier)) {
          return false;
        }
        companiaCollectionIdentifiers.push(companiaIdentifier);
        return true;
      });
      return [...companiasToAdd, ...companiaCollection];
    }
    return companiaCollection;
  }
}
