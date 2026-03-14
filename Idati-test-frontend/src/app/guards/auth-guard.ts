import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // ¿Está logueado?
  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  // ¿Tiene el rol requerido?
  const rolRequerido = route.data['rol'];
  if (rolRequerido && authService.getRol() !== rolRequerido) {
    // Si no tiene el rol redirige a su dashboard correspondiente
    if (authService.isAdmin()) {
      router.navigate(['/dashboard-admin']);
    } else {
      router.navigate(['/dashboard-empleado']);
    }
    return false;
  }

  return true;
};
