import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
//import { LoginAuthInterface } from './login-auth-interface';

@Injectable({
  providedIn: 'root',
})
export class LoginAuthService {

  private url = 'http://localhost:8080/auth';

  constructor(private http: HttpClient){}

  login(credenciales: {correoElectronico: string; contrasena: string}): Observable</*LoginAuthInterface*/string>{

    /*return this.http.post<LoginAuthInterface>(`${this.url}/login`, credenciales).pipe(tap(Response => {
      sessionStorage.setItem('token', Response.token);
      sessionStorage.setItem('emailUser', Response.correoElectronico);
      sessionStorage.setItem('roles', JSON.stringify(Response.roles));
      
    }));*/

    return this.http.post<string>(`${this.url}/login`, credenciales);

  }

}
