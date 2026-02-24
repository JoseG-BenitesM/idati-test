import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuariosService, Usuario } from '../../services/usuarios';
import { Observable, tap, catchError, of } from 'rxjs';

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
    // 2. Lo inicializamos dentro del ngOnInit
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
}