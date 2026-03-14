import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SolicitudService } from '../../services/solicitud';

@Component({
  selector: 'app-recuperar-contrasena',
  imports: [CommonModule, FormsModule],
  templateUrl: './recuperar-contrasena.html',
  styleUrl: './recuperar-contrasena.css',
})
export class RecuperarContrasena {
  correoOUsuario: string = '';
  mensaje: string = '';
  error: string = '';
  cargando: boolean = false;

  constructor(
    private solicitudService: SolicitudService,
    private router: Router,
  ) {}

  solicitar(): void {
    this.mensaje = '';
    this.error = '';

    if (!this.correoOUsuario) {
      this.error = 'Por favor ingresa tu correo o usuario.';
      return;
    }

    this.cargando = true;

    this.solicitudService.solicitar(this.correoOUsuario).subscribe({
      next: (response) => {
        this.cargando = false;
        this.mensaje = response.mensaje;
      },
      error: (err) => {
        this.cargando = false;
        if (err.status === 409) {
          this.error = 'Ya tienes una solicitud pendiente.';
        } else if (err.error?.error?.includes('LIMITE')) {
          this.error = 'Has superado el límite de solicitudes. Intenta en 1 hora.';
        } else {
          this.error = 'No se pudo procesar la solicitud.';
        }
      },
    });
  }

  irRestablecer(): void {
    this.router.navigate(['/restablecer-contrasena']);
  }

  volverLogin(): void {
    this.router.navigate(['/login']);
  }
}
