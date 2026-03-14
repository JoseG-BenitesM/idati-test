import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SolicitudService } from '../../services/solicitud';

@Component({
  selector: 'app-restablecer-contrasena',
  imports: [CommonModule, FormsModule],
  templateUrl: './restablecer-contrasena.html',
  styleUrl: './restablecer-contrasena.css',
})
export class RestablecerContrasena {
  correoOUsuario: string = '';
  codigo: string = '';
  nuevaContrasena: string = '';
  mensaje: string = '';
  error: string = '';
  cargando: boolean = false;
  exitoso: boolean = false;
  verContrasena: boolean = false;

  constructor(
    private solicitudService: SolicitudService,
    private router: Router,
  ) {}

  toggleContrasena(): void {
    this.verContrasena = !this.verContrasena;
  }

  restablecer(): void {
    this.mensaje = '';
    this.error = '';

    if (!this.correoOUsuario || !this.codigo || !this.nuevaContrasena) {
      this.error = 'Por favor completa todos los campos.';
      return;
    }

    if (this.nuevaContrasena.length < 6) {
      this.error = 'La contraseña debe tener al menos 6 caracteres.';
      return;
    }

    this.cargando = true;

    this.solicitudService
      .restablecer(this.correoOUsuario, this.codigo, this.nuevaContrasena)
      .subscribe({
        next: (response) => {
          this.cargando = false;
          this.mensaje = response.mensaje;
          this.exitoso = true;
        },
        error: (err) => {
          this.cargando = false;
          if (err.error?.error?.includes('EXPIRADO')) {
            this.error = 'El código ha expirado. Solicita uno nuevo.';
          } else {
            this.error = 'Código inválido o ya utilizado.';
          }
        },
      });
  }

  volverLogin(): void {
    this.router.navigate(['/login']);
  }
}
