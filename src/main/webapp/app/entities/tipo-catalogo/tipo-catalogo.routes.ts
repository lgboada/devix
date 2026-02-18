import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TipoCatalogoResolve from './route/tipo-catalogo-routing-resolve.service';

const tipoCatalogoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tipo-catalogo.component').then(m => m.TipoCatalogoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tipo-catalogo-detail.component').then(m => m.TipoCatalogoDetailComponent),
    resolve: {
      tipoCatalogo: TipoCatalogoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tipo-catalogo-update.component').then(m => m.TipoCatalogoUpdateComponent),
    resolve: {
      tipoCatalogo: TipoCatalogoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tipo-catalogo-update.component').then(m => m.TipoCatalogoUpdateComponent),
    resolve: {
      tipoCatalogo: TipoCatalogoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tipoCatalogoRoute;
