import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoProducto } from '../tipo-producto.model';
import { TipoProductoService } from '../service/tipo-producto.service';

const tipoProductoResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoProducto> => {
  const id = route.params.id;
  if (id) {
    return inject(TipoProductoService)
      .find(id)
      .pipe(
        mergeMap((tipoProducto: HttpResponse<ITipoProducto>) => {
          if (tipoProducto.body) {
            return of(tipoProducto.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default tipoProductoResolve;
