import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-dashboard-admin',
  imports: [CommonModule],
  templateUrl: './dashboard-admin.html',
  styleUrl: './dashboard-admin.css',
})
export class DashboardAdmin {
  usuario: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {
    this.usuario = this.authService.getUsuario() || '';
  }

  irGestionUsuarios(): void {
    this.router.navigate(['/gestion-usuarios']);
  }

  irGestionSolicitudes(): void {
    this.router.navigate(['/gestion-solicitudes']);
  }

  irHistorial(): void {
    this.router.navigate(['/historial-solicitudes']);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
