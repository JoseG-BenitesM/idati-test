import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const iTSInterceptor: HttpInterceptorFn = (req, next) => {

  const router = inject(Router);

  const token = sessionStorage.getItem('token');

  if(token){
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req).pipe(
    catchError(error => {
      if (error.status === 401){
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('usuario');
        sessionStorage.removeItem('rol');
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
