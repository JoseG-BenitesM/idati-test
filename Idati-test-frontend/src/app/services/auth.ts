import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  login(correoElectronico: string, contrasena: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/login`, {
      correoElectronico,
      contrasena,
    });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    localStorage.removeItem('rol');
  }

  guardarSesion(token: string, usuario: string, rol: string): void {
    localStorage.setItem('token', token);
    localStorage.setItem('usuario', usuario);
    localStorage.setItem('rol', rol);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsuario(): string | null {
    return localStorage.getItem('usuario');
  }

  getRol(): string | null {
    return localStorage.getItem('rol');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  isAdmin(): boolean {
    return this.getRol() === 'ROLE_ADMIN';
  }
}
