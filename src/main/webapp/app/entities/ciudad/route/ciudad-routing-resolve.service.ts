import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICiudad } from '../ciudad.model';
import { CiudadService } from '../service/ciudad.service';

const ciudadResolve = (route: ActivatedRouteSnapshot): Observable<null | ICiudad> => {
  const id = route.params.id;
  if (id) {
    return inject(CiudadService)
      .find(id)
      .pipe(
        mergeMap((ciudad: HttpResponse<ICiudad>) => {
          if (ciudad.body) {
            return of(ciudad.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ciudadResolve;
