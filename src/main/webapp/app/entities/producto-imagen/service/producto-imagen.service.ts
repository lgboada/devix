import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IProductoImagen, NewProductoImagen } from '../producto-imagen.model';

export type EntityResponseType = HttpResponse<IProductoImagen>;
export type EntityArrayResponseType = HttpResponse<IProductoImagen[]>;

@Injectable({ providedIn: 'root' })
export class ProductoImagenService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/productos');

  queryByProducto(productoId: number): Observable<EntityArrayResponseType> {
    return this.http.get<IProductoImagen[]>(`${this.resourceUrl}/${productoId}/imagenes`, { observe: 'response' });
  }

  create(productoId: number, imagen: NewProductoImagen): Observable<EntityResponseType> {
    return this.http.post<IProductoImagen>(`${this.resourceUrl}/${productoId}/imagenes`, imagen, { observe: 'response' });
  }

  update(productoId: number, imagen: IProductoImagen): Observable<EntityResponseType> {
    return this.http.put<IProductoImagen>(`${this.resourceUrl}/${productoId}/imagenes/${imagen.id}`, imagen, { observe: 'response' });
  }

  delete(productoId: number, imagenId: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${productoId}/imagenes/${imagenId}`, { observe: 'response' });
  }
}
