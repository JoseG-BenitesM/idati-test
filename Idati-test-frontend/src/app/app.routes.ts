import { Routes } from '@angular/router';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./components/login/login').then((m) => m.Login),
  },
  {
    path: 'recuperar-contrasena',
    loadComponent: () =>
      import('./components/recuperar-contrasena/recuperar-contrasena').then(
        (m) => m.RecuperarContrasena,
      ),
  },
  {
    path: 'restablecer-contrasena',
    loadComponent: () =>
      import('./components/restablecer-contrasena/restablecer-contrasena').then(
        (m) => m.RestablecerContrasena,
      ),
  },
  {
    path: 'dashboard-empleado',
    loadComponent: () =>
      import('./components/dashboard-empleado/dashboard-empleado').then((m) => m.DashboardEmpleado),
    canActivate: [authGuard],
  },
  {
    path: 'dashboard-admin',
    loadComponent: () =>
      import('./components/dashboard-admin/dashboard-admin').then((m) => m.DashboardAdmin),
    canActivate: [authGuard],
    data: { rol: 'ROLE_ADMIN' },
  },
  {
    path: 'gestion-usuarios',
    loadComponent: () =>
      import('./components/gestion-usuarios/gestion-usuarios').then((m) => m.GestionUsuarios),
    canActivate: [authGuard],
    data: { rol: 'ROLE_ADMIN' },
  },
  {
    path: 'gestion-solicitudes',
    loadComponent: () =>
      import('./components/gestion-solicitudes/gestion-solicitudes').then(
        (m) => m.GestionSolicitudes,
      ),
    canActivate: [authGuard],
    data: { rol: 'ROLE_ADMIN' },
  },
  {
    path: 'historial-solicitudes',
    loadComponent: () =>
      import('./components/historial-solicitudes/historial-solicitudes').then(
        (m) => m.HistorialSolicitudes,
      ),
    canActivate: [authGuard],
    data: { rol: 'ROLE_ADMIN' },
  },
  { path: '**', redirectTo: 'login' },
];
