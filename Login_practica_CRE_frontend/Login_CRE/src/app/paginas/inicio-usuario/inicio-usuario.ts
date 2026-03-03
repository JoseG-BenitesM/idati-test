import { Component, OnInit } from '@angular/core';
import { Usuario } from '../../autenticacion/seguridad/servicios/usuarios/usuario-interface';
import { Proteccion } from '../../autenticacion/seguridad/servicios/PPTR/proteccion';
import { UsuariosService } from '../../autenticacion/seguridad/servicios/usuarios/usuarios-service';
import { Router } from '@angular/router';
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-inicio-usuario',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './inicio-usuario.html',
  styleUrl: './inicio-usuario.css',
})
export class InicioUsuario implements OnInit {

  nombreUsuario: string | null = null;

  constructor(private proteccion: Proteccion, private router: Router) {}

  ngOnInit(): void {
    this.nombreUsuario = this.proteccion.getUser();
  }

  logout(): void {
    this.proteccion.logout(); 
    this.router.navigate(['/login']); 
  }

}
