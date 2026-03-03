import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from '../../direcciones/environment.prod';
import { Usuario } from './usuario-interface';

@Injectable({
  providedIn: 'root',
})
export class UsuariosService {
  
  private apiUrl = `${environment.apiURL}/usuarios`;

  constructor(private http: HttpClient) {}

  obtenerUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }

  bloquearUsuario(id: number): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.apiUrl}/${id}/bloquear`, {});
  }

  desbloquearUsuario(id: number): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.apiUrl}/${id}/desbloquear`, {});
  }
}
