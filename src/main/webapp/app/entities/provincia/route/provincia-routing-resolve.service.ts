import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProvincia } from '../provincia.model';
import { ProvinciaService } from '../service/provincia.service';

const provinciaResolve = (route: ActivatedRouteSnapshot): Observable<null | IProvincia> => {
  const id = route.params.id;
  if (id) {
    return inject(ProvinciaService)
      .find(id)
      .pipe(
        mergeMap((provincia: HttpResponse<IProvincia>) => {
          if (provincia.body) {
            return of(provincia.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default provinciaResolve;
