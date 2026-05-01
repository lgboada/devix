import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import categoriaPacienteResolve from './route/categoria-paciente-routing-resolve.service';

const categoriaPacienteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/categoria-paciente.component').then(m => m.CategoriaPacienteComponent),
    data: { defaultSort: `id,${ASC}` },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/categoria-paciente-detail.component').then(m => m.CategoriaPacienteDetailComponent),
    resolve: { categoriaPaciente: categoriaPacienteResolve },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/categoria-paciente-update.component').then(m => m.CategoriaPacienteUpdateComponent),
    resolve: { categoriaPaciente: categoriaPacienteResolve },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/categoria-paciente-update.component').then(m => m.CategoriaPacienteUpdateComponent),
    resolve: { categoriaPaciente: categoriaPacienteResolve },
    canActivate: [UserRouteAccessService],
  },
];

export default categoriaPacienteRoute;
