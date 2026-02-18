import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoCliente, NewTipoCliente } from '../tipo-cliente.model';

export type PartialUpdateTipoCliente = Partial<ITipoCliente> & Pick<ITipoCliente, 'id'>;

export type EntityResponseType = HttpResponse<ITipoCliente>;
export type EntityArrayResponseType = HttpResponse<ITipoCliente[]>;

@Injectable({ providedIn: 'root' })
export class TipoClienteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-clientes');

  create(tipoCliente: NewTipoCliente): Observable<EntityResponseType> {
    return this.http.post<ITipoCliente>(this.resourceUrl, tipoCliente, { observe: 'response' });
  }

  update(tipoCliente: ITipoCliente): Observable<EntityResponseType> {
    return this.http.put<ITipoCliente>(`${this.resourceUrl}/${this.getTipoClienteIdentifier(tipoCliente)}`, tipoCliente, {
      observe: 'response',
    });
  }

  partialUpdate(tipoCliente: PartialUpdateTipoCliente): Observable<EntityResponseType> {
    return this.http.patch<ITipoCliente>(`${this.resourceUrl}/${this.getTipoClienteIdentifier(tipoCliente)}`, tipoCliente, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoCliente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoCliente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTipoClienteIdentifier(tipoCliente: Pick<ITipoCliente, 'id'>): number {
    return tipoCliente.id;
  }

  compareTipoCliente(o1: Pick<ITipoCliente, 'id'> | null, o2: Pick<ITipoCliente, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoClienteIdentifier(o1) === this.getTipoClienteIdentifier(o2) : o1 === o2;
  }

  addTipoClienteToCollectionIfMissing<Type extends Pick<ITipoCliente, 'id'>>(
    tipoClienteCollection: Type[],
    ...tipoClientesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoClientes: Type[] = tipoClientesToCheck.filter(isPresent);
    if (tipoClientes.length > 0) {
      const tipoClienteCollectionIdentifiers = tipoClienteCollection.map(tipoClienteItem => this.getTipoClienteIdentifier(tipoClienteItem));
      const tipoClientesToAdd = tipoClientes.filter(tipoClienteItem => {
        const tipoClienteIdentifier = this.getTipoClienteIdentifier(tipoClienteItem);
        if (tipoClienteCollectionIdentifiers.includes(tipoClienteIdentifier)) {
          return false;
        }
        tipoClienteCollectionIdentifiers.push(tipoClienteIdentifier);
        return true;
      });
      return [...tipoClientesToAdd, ...tipoClienteCollection];
    }
    return tipoClienteCollection;
  }
}
