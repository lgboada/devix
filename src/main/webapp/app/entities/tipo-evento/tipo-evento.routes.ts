import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TipoEventoResolve from './route/tipo-evento-routing-resolve.service';

const tipoEventoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tipo-evento.component').then(m => m.TipoEventoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tipo-evento-detail.component').then(m => m.TipoEventoDetailComponent),
    resolve: {
      tipoEvento: TipoEventoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tipo-evento-update.component').then(m => m.TipoEventoUpdateComponent),
    resolve: {
      tipoEvento: TipoEventoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tipo-evento-update.component').then(m => m.TipoEventoUpdateComponent),
    resolve: {
      tipoEvento: TipoEventoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tipoEventoRoute;
