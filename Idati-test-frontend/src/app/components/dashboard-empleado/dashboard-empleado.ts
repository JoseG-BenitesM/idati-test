import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-dashboard-empleado',
  imports: [CommonModule],
  templateUrl: './dashboard-empleado.html',
  styleUrl: './dashboard-empleado.css',
})
export class DashboardEmpleado {
  usuario: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {
    this.usuario = this.authService.getUsuario() || '';
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
