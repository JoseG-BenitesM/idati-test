import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SolicitudService } from '../../services/solicitud';

@Component({
  selector: 'app-gestion-solicitudes',
  imports: [CommonModule],
  templateUrl: './gestion-solicitudes.html',
  styleUrl: './gestion-solicitudes.css',
})
export class GestionSolicitudes implements OnInit {
  solicitudes: any[] = [];
  cargando: boolean = false;
  error: string = '';
  mensaje: string = '';
  codigoGenerado: string = '';
  usuarioAprobado: string = '';

  constructor(
    private solicitudService: SolicitudService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.cargando = true;
    this.error = '';

    this.solicitudService.listarPendientes().subscribe({
      next: (data) => {
        // Incluye estado=0 (pendientes) y estado=4 (correo falló)
        this.solicitudes = data;
        this.cargando = false;
      },
      error: (err) => {
        this.cargando = false;
        if (err.status === 204) {
          this.solicitudes = [];
        } else {
          this.error = 'Error al cargar solicitudes.';
        }
      },
    });
  }

  aprobar(id: number, nombre: string): void {
    if (!confirm(`¿Aprobar la solicitud de ${nombre}?`)) return;

    this.codigoGenerado = '';
    this.usuarioAprobado = '';

    this.solicitudService.aprobar(id).subscribe({
      next: (response) => {
        this.mensaje = response.mensaje;
        this.codigoGenerado = response.codigo;
        this.usuarioAprobado = nombre;
        this.cargarSolicitudes();
        setTimeout(() => {
          this.mensaje = '';
          this.codigoGenerado = '';
          this.usuarioAprobado = '';
        }, 15000);
      },
      error: () => {
        this.error = 'Error al aprobar la solicitud.';
      },
    });
  }

  reenviar(id: number, nombre: string): void {
    if (!confirm(`¿Reenviar código a ${nombre}?`)) return;

    this.solicitudService.reenviar(id).subscribe({
      next: (response) => {
        this.mensaje = response.mensaje;
        this.cargarSolicitudes();
        setTimeout(() => (this.mensaje = ''), 5000);
      },
      error: (err) => {
        if (err.error?.error?.includes('EXPIRADO')) {
          this.error = 'El código expiró. El usuario debe solicitar uno nuevo.';
        } else {
          this.error = 'Error al reenviar el código.';
        }
      },
    });
  }

  getEstadoLabel(estado: number): string {
    switch (estado) {
      case 0:
        return 'Pendiente';
      case 4:
        return 'Correo falló';
      default:
        return 'Desconocido';
    }
  }

  getEstadoClass(estado: number): string {
    switch (estado) {
      case 0:
        return 'bg-warning text-dark';
      case 4:
        return 'bg-danger';
      default:
        return 'bg-secondary';
    }
  }

  volver(): void {
    this.router.navigate(['/dashboard-admin']);
  }

  paginaActual: number = 1;
  itemsPorPagina: number = 5;

  get solicitudesPaginadas(): any[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.solicitudes.slice(inicio, fin);
  }

  get totalPaginas(): number[] {
    const total = Math.ceil(this.solicitudes.length / this.itemsPorPagina);
    return Array.from({ length: total }, (_, i) => i + 1);
  }

  cambiarPagina(pagina: number): void {
    this.paginaActual = pagina;
  }
}
