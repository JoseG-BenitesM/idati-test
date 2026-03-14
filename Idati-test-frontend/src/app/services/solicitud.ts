import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root',
})
export class SolicitudService {
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

  // ─── Públicos (sin token) ───────────────────────────────────

  solicitar(correoOUsuario: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/api/solicitudes/solicitar`, {
      correoOUsuario,
    });
  }

  restablecer(correoOUsuario: string, codigo: string, nuevaContrasena: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/api/solicitudes/restablecer`, {
      correoOUsuario,
      codigo,
      nuevaContrasena,
    });
  }
  // ─── Solo ROLE_ADMIN (con token) ───────────────────────────

  listarPendientes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/solicitudes/pendientes`, {
      headers: this.getHeaders(),
    });
  }

  listarTodas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/solicitudes`, {
      headers: this.getHeaders(),
    });
  }

  aprobar(id: number): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/api/solicitudes/${id}/aprobar`,
      {},
      {
        headers: this.getHeaders(),
      },
    );
  }

  reenviar(id: number): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/api/solicitudes/${id}/reenviar`,
      {},
      {
        headers: this.getHeaders(),
      },
    );
  }
}
