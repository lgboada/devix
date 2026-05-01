import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategoriaPaciente } from '../categoria-paciente.model';
import { CategoriaPacienteService } from '../service/categoria-paciente.service';

const categoriaPacienteResolve = (route: ActivatedRouteSnapshot): Observable<null | ICategoriaPaciente> => {
  const id = route.params.id;
  if (id) {
    return inject(CategoriaPacienteService)
      .find(id)
      .pipe(
        mergeMap((res: HttpResponse<ICategoriaPaciente>) => {
          if (res.body) return of(res.body);
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default categoriaPacienteResolve;
