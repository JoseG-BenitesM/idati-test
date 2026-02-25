import { Component, OnInit } from '@angular/core';
import { listaPendientes } from '../../autenticacion/seguridad/servicios/solicitudes/solicitud-interface';
import { SolicitudService } from '../../autenticacion/seguridad/servicios/solicitudes/solicitud-service';
import { catchError, Observable, of, tap } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-lista-pendientes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-pendientes.html',
  styleUrls: ['./lista-pendientes.css'],
})
export class ListaPendientes implements OnInit{
  pendientes$!: Observable<listaPendientes[]>;
  error: string = '';

  constructor(private SLS: SolicitudService) {}

  ngOnInit(): void {
    this.cargarPendientes();
  }

  cargarPendientes(): void {
    this.pendientes$ = this.SLS.listarPendientes().pipe(
      tap(data => console.log('DATA recibida:', data)),
      catchError(err => {
        console.error('Error al obtener pendientes:', err);
        this.error = 'Error de conexión';
        return of([]);
      })
    );
  }

  desbloquear(id: number): void {
    this.SLS.aprobacion(id).subscribe({
      next: (res) => {
        console.log(`Pendiente ${id} desbloqueado`);
        console.log("codigos:", res);
        this.cargarPendientes();
      },
      error: (err) => {
        console.error('Error al desbloquear:', err);
        this.error = 'No se pudo desbloquear';
      }
    });
  }
}
