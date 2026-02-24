import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuariosService } from '../../services/usuarios';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './usuarios.html',
  styleUrls: ['./usuarios.css']
})
export class UsuariosComponent implements OnInit {

  listaUsuarios: any[] = [];
  cargando: boolean = true;
  error: string = '';

  constructor(private usuariosService: UsuariosService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.usuariosService.obtenerUsuarios().subscribe({
      next: (data) => {
        this.listaUsuarios = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Error al cargar los usuarios';
        this.cargando = false;
      }
    });
  }
}