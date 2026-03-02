import { Injectable } from '@angular/core';
import { environment } from '../../direcciones/environment.prod';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { listaPendientes } from './solicitud-interface';

@Injectable({
  providedIn: 'root',
})
export class SolicitudService {
  
  private apiURL = `${environment.apiURL}/solicitudes`;

  constructor(private http: HttpClient) {}

  solictarRecuperacion(correoOUsuario: string): Observable<any>{
    return this.http.post(`${this.apiURL}/solicitar`, {correoOUsuario: correoOUsuario});
  }
  
  listarPendientes(): Observable<listaPendientes[]>{
    return this.http.get<listaPendientes[]>(`${this.apiURL}/pendientes`);
  }

  aprobacion(id: number): Observable<any>{
    return this.http.patch(`${this.apiURL}/${id}/aprobar`, {});
  } 

  restablecer(correo: string, codigo: string, nuevacontrasena: string): Observable<any>{
    return this.http.post(`${this.apiURL}/restablecer`, {
      correoOUsuario: correo,
      codigo: codigo,
      nuevaContrasena: nuevacontrasena
    });
  }

}
