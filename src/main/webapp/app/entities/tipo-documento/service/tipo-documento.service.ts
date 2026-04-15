import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoDocumento } from '../tipo-documento.model';

export type EntityResponseType = HttpResponse<ITipoDocumento>;
export type EntityArrayResponseType = HttpResponse<ITipoDocumento[]>;

@Injectable({ providedIn: 'root' })
export class TipoDocumentoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-documentos');

  create(tipoDocumento: ITipoDocumento): Observable<EntityResponseType> {
    return this.http.post<ITipoDocumento>(this.resourceUrl, tipoDocumento, { observe: 'response' });
  }

  update(tipoDocumento: ITipoDocumento): Observable<EntityResponseType> {
    return this.http.put<ITipoDocumento>(`${this.resourceUrl}/${tipoDocumento.noCia}/${tipoDocumento.tipoDocumento}`, tipoDocumento, {
      observe: 'response',
    });
  }

  find(noCia: number, codigo: string): Observable<EntityResponseType> {
    return this.http.get<ITipoDocumento>(`${this.resourceUrl}/${noCia}/${codigo}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoDocumento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(noCia: number, codigo: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${noCia}/${codigo}`, { observe: 'response' });
  }

  /** Clave única para tracking */
  getKey(item: ITipoDocumento): string {
    return `${item.noCia}_${item.tipoDocumento}`;
  }
}
