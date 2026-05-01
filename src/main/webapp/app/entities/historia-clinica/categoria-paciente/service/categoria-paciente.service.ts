import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategoriaPaciente, NewCategoriaPaciente } from '../categoria-paciente.model';

export type PartialUpdateCategoriaPaciente = Partial<ICategoriaPaciente> & Pick<ICategoriaPaciente, 'id'>;
export type EntityResponseType = HttpResponse<ICategoriaPaciente>;
export type EntityArrayResponseType = HttpResponse<ICategoriaPaciente[]>;

@Injectable({ providedIn: 'root' })
export class CategoriaPacienteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categoria-pacientes');

  create(categoriaPaciente: NewCategoriaPaciente): Observable<EntityResponseType> {
    return this.http.post<ICategoriaPaciente>(this.resourceUrl, categoriaPaciente, { observe: 'response' });
  }

  update(categoriaPaciente: ICategoriaPaciente): Observable<EntityResponseType> {
    return this.http.put<ICategoriaPaciente>(
      `${this.resourceUrl}/${this.getCategoriaPacienteIdentifier(categoriaPaciente)}`,
      categoriaPaciente,
      { observe: 'response' },
    );
  }

  partialUpdate(categoriaPaciente: PartialUpdateCategoriaPaciente): Observable<EntityResponseType> {
    return this.http.patch<ICategoriaPaciente>(
      `${this.resourceUrl}/${this.getCategoriaPacienteIdentifier(categoriaPaciente)}`,
      categoriaPaciente,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategoriaPaciente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoriaPaciente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCategoriaPacienteIdentifier(categoriaPaciente: Pick<ICategoriaPaciente, 'id'>): number {
    return categoriaPaciente.id;
  }

  compareCategoriaPaciente(o1: Pick<ICategoriaPaciente, 'id'> | null, o2: Pick<ICategoriaPaciente, 'id'> | null): boolean {
    return o1 && o2 ? this.getCategoriaPacienteIdentifier(o1) === this.getCategoriaPacienteIdentifier(o2) : o1 === o2;
  }

  addCategoriaPacienteToCollectionIfMissing<Type extends Pick<ICategoriaPaciente, 'id'>>(
    collection: Type[],
    ...toCheck: (Type | null | undefined)[]
  ): Type[] {
    const items: Type[] = toCheck.filter(isPresent);
    if (items.length > 0) {
      const identifiers = collection.map(item => this.getCategoriaPacienteIdentifier(item));
      const toAdd = items.filter(item => {
        const id = this.getCategoriaPacienteIdentifier(item);
        if (identifiers.includes(id)) return false;
        identifiers.push(id);
        return true;
      });
      return [...toAdd, ...collection];
    }
    return collection;
  }
}
