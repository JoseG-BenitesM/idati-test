import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { LoginAuthInterface } from './login-auth-interface';
import { environment } from '../../direcciones/environment.prod';

@Injectable({
  providedIn: 'root',
})
export class LoginAuthService {

  private url = `${environment.authUrl}`;

  constructor(private http: HttpClient){}

  login(credenciales: {correoElectronico: string; contrasena: string}): Observable<LoginAuthInterface>{

    return this.http.post<LoginAuthInterface>(`${this.url}/login`, credenciales).pipe(tap(Response => {
      sessionStorage.setItem('token', Response.token);
      sessionStorage.setItem('usuario', Response.usuario);
      sessionStorage.setItem('rol', Response.rol);
      
    }));

  }

}
