import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IModelo, NewModelo } from '../modelo.model';

export type PartialUpdateModelo = Partial<IModelo> & Pick<IModelo, 'id'>;

export type EntityResponseType = HttpResponse<IModelo>;
export type EntityArrayResponseType = HttpResponse<IModelo[]>;

@Injectable({ providedIn: 'root' })
export class ModeloService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/modelos');

  create(modelo: NewModelo): Observable<EntityResponseType> {
    return this.http.post<IModelo>(this.resourceUrl, modelo, { observe: 'response' });
  }

  update(modelo: IModelo): Observable<EntityResponseType> {
    return this.http.put<IModelo>(`${this.resourceUrl}/${this.getModeloIdentifier(modelo)}`, modelo, { observe: 'response' });
  }

  partialUpdate(modelo: PartialUpdateModelo): Observable<EntityResponseType> {
    return this.http.patch<IModelo>(`${this.resourceUrl}/${this.getModeloIdentifier(modelo)}`, modelo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IModelo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IModelo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getModeloIdentifier(modelo: Pick<IModelo, 'id'>): number {
    return modelo.id;
  }

  compareModelo(o1: Pick<IModelo, 'id'> | null, o2: Pick<IModelo, 'id'> | null): boolean {
    return o1 && o2 ? this.getModeloIdentifier(o1) === this.getModeloIdentifier(o2) : o1 === o2;
  }

  addModeloToCollectionIfMissing<Type extends Pick<IModelo, 'id'>>(
    modeloCollection: Type[],
    ...modelosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const modelos: Type[] = modelosToCheck.filter(isPresent);
    if (modelos.length > 0) {
      const modeloCollectionIdentifiers = modeloCollection.map(modeloItem => this.getModeloIdentifier(modeloItem));
      const modelosToAdd = modelos.filter(modeloItem => {
        const modeloIdentifier = this.getModeloIdentifier(modeloItem);
        if (modeloCollectionIdentifiers.includes(modeloIdentifier)) {
          return false;
        }
        modeloCollectionIdentifiers.push(modeloIdentifier);
        return true;
      });
      return [...modelosToAdd, ...modeloCollection];
    }
    return modeloCollection;
  }
}
