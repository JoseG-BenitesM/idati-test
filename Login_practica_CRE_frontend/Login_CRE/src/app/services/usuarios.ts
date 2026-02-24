import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Usuario {
  id: number;
  usuario_nombre: string;
  correo_electronico: string;
  estado_usuario: string;
  intentos_fallidos: number;
}

@Injectable({
  providedIn: 'root',
})
export class UsuariosService {

  private apiUrl = 'http://localhost:8080/api/usuarios'; // cambia si tu endpoint es distinto

  constructor(private http: HttpClient) {}

  obtenerUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }
}