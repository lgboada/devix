import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILineaNegocio } from '../linea-negocio.model';

export type EntityResponseType = HttpResponse<ILineaNegocio>;
export type EntityArrayResponseType = HttpResponse<ILineaNegocio[]>;

@Injectable({ providedIn: 'root' })
export class LineaNegocioService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/linea-negocios');

  create(lineaNegocio: ILineaNegocio): Observable<EntityResponseType> {
    return this.http.post<ILineaNegocio>(this.resourceUrl, lineaNegocio, { observe: 'response' });
  }

  update(lineaNegocio: ILineaNegocio): Observable<EntityResponseType> {
    return this.http.put<ILineaNegocio>(`${this.resourceUrl}/${lineaNegocio.noCia}/${lineaNegocio.lineaNo}`, lineaNegocio, {
      observe: 'response',
    });
  }

  find(noCia: number, lineaNo: string): Observable<EntityResponseType> {
    return this.http.get<ILineaNegocio>(`${this.resourceUrl}/${noCia}/${lineaNo}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILineaNegocio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(noCia: number, lineaNo: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${noCia}/${lineaNo}`, { observe: 'response' });
  }

  /** Clave única para tracking */
  getKey(item: ILineaNegocio): string {
    return `${item.noCia}_${item.lineaNo}`;
  }
}
