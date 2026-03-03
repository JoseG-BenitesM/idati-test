import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Usuario } from '../../autenticacion/seguridad/servicios/usuarios/usuario-interface';
import { Observable, tap, catchError, of, switchMap } from 'rxjs';
import { UsuariosService } from '../../autenticacion/seguridad/servicios/usuarios/usuarios-service';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './usuarios.html',
  styleUrls: ['./usuarios.css']
})
export class UsuariosComponent implements OnInit {
  // 1. Declaramos el Observable sin inicializarlo arriba
  listaUsuarios$!: Observable<Usuario[]>;
  cargando: boolean = true;
  error: string = '';

  constructor(private usuariosService: UsuariosService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.listaUsuarios$ = this.usuariosService.obtenerUsuarios().pipe(
      tap(data => {
        console.log('¡Llegaron los datos!', data);
        this.cargando = false;
      }),
      catchError(err => {
        console.error('Error en el stream:', err);
        this.error = 'Error de conexión';
        this.cargando = false;
        return of([]); // Retorna array vacío para que el pipe async no rompa
      })
    );
  }

  bloquear(id: number): void {
    this.listaUsuarios$ = this.usuariosService.bloquearUsuario(id).pipe(
      switchMap(() => this.usuariosService.obtenerUsuarios()), // 👈 refresca después del PATCH
      tap(() => this.cargando = false),
        catchError(err => {
          console.error('Error al bloquear', err);
          this.error = 'Error de conexión';
          this.cargando = false;
          return of([]);
      })
    );
  }

  desbloquear(id: number): void {
    this.listaUsuarios$ = this.usuariosService.desbloquearUsuario(id).pipe(
      switchMap(() => this.usuariosService.obtenerUsuarios()), // 👈 refresca después del PATCH
      tap(() => this.cargando = false),
      catchError(err => {
        console.error('Error al desbloquear', err);
        this.error = 'Error de conexión';
        this.cargando = false;
        return of([]);
      })
    );
  }

}