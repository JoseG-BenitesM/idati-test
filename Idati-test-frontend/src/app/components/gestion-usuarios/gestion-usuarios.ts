import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario';

@Component({
  selector: 'app-gestion-usuarios',
  imports: [CommonModule],
  templateUrl: './gestion-usuarios.html',
  styleUrl: './gestion-usuarios.css',
})
export class GestionUsuarios implements OnInit {
  usuarios: any[] = [];
  cargando: boolean = false;
  error: string = '';
  mensaje: string = '';

  constructor(
    private usuarioService: UsuarioService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.cargando = true;
    this.error = '';

    this.usuarioService.listar().subscribe({
      next: (data) => {
        this.usuarios = data;
        this.cargando = false;
      },
      error: (err) => {
        this.cargando = false;
        this.error = 'Error al cargar usuarios.';
      },
    });
  }

  bloquear(id: number, nombre: string): void {
    if (!confirm(`¿Bloquear al usuario ${nombre}?`)) return;

    this.usuarioService.bloquear(id).subscribe({
      next: () => {
        this.mensaje = `Usuario ${nombre} bloqueado correctamente.`;
        this.cargarUsuarios();
        setTimeout(() => (this.mensaje = ''), 3000);
      },
      error: () => {
        this.error = 'Error al bloquear usuario.';
      },
    });
  }

  desbloquear(id: number, nombre: string): void {
    if (!confirm(`¿Desbloquear al usuario ${nombre}?`)) return;

    this.usuarioService.desbloquear(id).subscribe({
      next: () => {
        this.mensaje = `Usuario ${nombre} desbloqueado correctamente.`;
        this.cargarUsuarios();
        setTimeout(() => (this.mensaje = ''), 3000);
      },
      error: () => {
        this.error = 'Error al desbloquear usuario.';
      },
    });
  }

  volver(): void {
    this.router.navigate(['/dashboard-admin']);
  }

  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 3;

  get usuariosPaginados(): any[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.usuarios.slice(inicio, fin);
    // slice() → corta el array según la página actual
  }

  get totalPaginas(): number[] {
    const total = Math.ceil(this.usuarios.length / this.itemsPorPagina);
    return Array.from({ length: total }, (_, i) => i + 1);
    // Array.from → genera [1, 2, 3...] según el total de páginas
  }

  cambiarPagina(pagina: number): void {
    this.paginaActual = pagina;
  }
}
