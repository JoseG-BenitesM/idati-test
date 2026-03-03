import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Proteccion } from './seguridad/servicios/PPTR/proteccion';

export const sPTRGuard: CanActivateFn = (route, state) => {
  const authService = inject(Proteccion);
  const router = inject(Router);

  const token = authService.getToken();
  const role = authService.getUserRole();

  if (!token) {
    router.navigate(['/login']);
    return false;
  }

  const requiresAdmin = route.data['requiresAdmin'] === true;
  if (requiresAdmin && role !== 'ROLE_ADMIN') {
    router.navigate(['/login']);
    return false;
  }

  return true;
};
