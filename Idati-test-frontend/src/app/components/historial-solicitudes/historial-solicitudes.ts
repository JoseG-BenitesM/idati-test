import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SolicitudService } from '../../services/solicitud';

@Component({
  selector: 'app-historial-solicitudes',
  imports: [CommonModule],
  templateUrl: './historial-solicitudes.html',
  styleUrl: './historial-solicitudes.css',
})
export class HistorialSolicitudes implements OnInit {
  solicitudes: any[] = [];
  cargando: boolean = false;
  error: string = '';
  mensaje: string = '';

  constructor(
    private solicitudService: SolicitudService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.cargarHistorial();
  }

  cargarHistorial(): void {
    this.cargando = true;
    this.error = '';

    this.solicitudService.listarTodas().subscribe({
      next: (data) => {
        this.solicitudes = data;
        this.cargando = false;
      },
      error: (err) => {
        this.cargando = false;
        if (err.status === 204) {
          this.solicitudes = [];
        } else {
          this.error = 'Error al cargar el historial.';
        }
      },
    });
  }
  reenviar(id: number, nombre: string): void {
    if (!confirm(`¿Reenviar código a ${nombre}?`)) return;

    this.solicitudService.reenviar(id).subscribe({
      next: (response) => {
        this.mensaje = response.mensaje;
        this.cargarHistorial();
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
  codigosVisibles: { [key: number]: boolean } = {};

  toggleCodigo(id: number): void {
    this.codigosVisibles[id] = !this.codigosVisibles[id];
  }

  getEstadoLabel(estado: number): string {
    switch (estado) {
      case 0:
        return 'Pendiente';
      case 1:
        return 'Aprobada';
      case 2:
        return 'Usada';
      case 3:
        return 'Expirada';
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
      case 1:
        return 'bg-info text-dark';
      case 2:
        return 'bg-success';
      case 3:
        return 'bg-secondary';
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
