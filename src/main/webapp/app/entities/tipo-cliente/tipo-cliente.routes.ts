import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TipoClienteResolve from './route/tipo-cliente-routing-resolve.service';

const tipoClienteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tipo-cliente.component').then(m => m.TipoClienteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tipo-cliente-detail.component').then(m => m.TipoClienteDetailComponent),
    resolve: {
      tipoCliente: TipoClienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tipo-cliente-update.component').then(m => m.TipoClienteUpdateComponent),
    resolve: {
      tipoCliente: TipoClienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tipo-cliente-update.component').then(m => m.TipoClienteUpdateComponent),
    resolve: {
      tipoCliente: TipoClienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tipoClienteRoute;
