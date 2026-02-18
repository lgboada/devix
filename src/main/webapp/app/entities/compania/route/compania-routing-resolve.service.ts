import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompania } from '../compania.model';
import { CompaniaService } from '../service/compania.service';

const companiaResolve = (route: ActivatedRouteSnapshot): Observable<null | ICompania> => {
  const id = route.params.id;
  if (id) {
    return inject(CompaniaService)
      .find(id)
      .pipe(
        mergeMap((compania: HttpResponse<ICompania>) => {
          if (compania.body) {
            return of(compania.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default companiaResolve;
