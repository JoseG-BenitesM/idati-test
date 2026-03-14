import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true, // Asegúrate de tener esto si usas imports directos
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  correoElectronico: string = '';
  contrasena: string = '';
  error: string = '';
  cargando: boolean = false;
  verContrasena: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  toggleContrasena(): void {
    this.verContrasena = !this.verContrasena;
  }

  login(): void {
    this.error = '';

    // 1. Validar si el campo de usuario/correo está vacío
    if (!this.correoElectronico || this.correoElectronico.trim() === '') {
      this.error = 'Falta rellenar el campo de usuario o correo.';
      return;
    }

    
    // 3. Validar si el campo de contraseña está vacío
    if (!this.contrasena || this.contrasena.trim() === '') {
      this.error = 'Falta rellenar el campo de la contraseña.';
      return;
    }

    // 4. Si todo es correcto, proceder con la petición al servidor
    this.cargando = true;

    this.authService.login(this.correoElectronico, this.contrasena).subscribe({
      next: (response) => {
        this.cargando = false;
        this.authService.guardarSesion(response.token, response.usuario, response.rol);
        
        if (response.rol === 'ROLE_ADMIN') {
          this.router.navigate(['/dashboard-admin']);
        } else {
          this.router.navigate(['/dashboard-empleado']);
        }
      },
      error: (err) => {
        this.cargando = false;
        if (err.status === 423) {
          this.error = 'Tu cuenta está bloqueada. Contacta al administrador.';
        } else if (err.status === 401 || err.status === 403) {
          // 401 es el estándar para credenciales que no coinciden
          this.error = 'Las credenciales son incorrectas. Verifica tu usuario y contraseña.';
        } else {
          this.error = 'Error al intentar ingresar. Por favor, intente más tarde.';
        }
      },
    });
  }

  irRecuperar(): void {
    this.router.navigate(['/recuperar-contrasena']);
  }
}