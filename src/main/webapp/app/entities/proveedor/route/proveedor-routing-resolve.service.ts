import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';

const proveedorResolve = (route: ActivatedRouteSnapshot): Observable<null | IProveedor> => {
  const id = route.params.id;
  if (id) {
    return inject(ProveedorService)
      .find(id)
      .pipe(
        mergeMap((proveedor: HttpResponse<IProveedor>) => {
          if (proveedor.body) {
            return of(proveedor.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default proveedorResolve;
