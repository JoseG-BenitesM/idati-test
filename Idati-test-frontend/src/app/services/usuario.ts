import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`,
    });
  }
  // El token se adjunta automáticamente en cada request
  // para que Spring Security lo valide en JwtAuthFilter

  listar(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/usuarios`, {
      headers: this.getHeaders(),
    });
  }

  bloquear(id: number): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/api/usuarios/${id}/bloquear`,
      {},
      {
        headers: this.getHeaders(),
      },
    );
  }

  desbloquear(id: number): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/api/usuarios/${id}/desbloquear`,
      {},
      {
        headers: this.getHeaders(),
      },
    );
  }
}
