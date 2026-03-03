import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from "@angular/router";
import { Proteccion } from '../../autenticacion/seguridad/servicios/PPTR/proteccion';

@Component({
  selector: 'app-inicio-admin',
  imports: [RouterLink, RouterOutlet],
  templateUrl: './inicio-admin.html',
  styleUrl: './inicio-admin.css',
})
export class InicioAdmin {

  constructor(private authService: Proteccion, private router: Router) {}

  logout(): void {
    this.authService.logout(); 
    this.router.navigate(['/login']); 
  }
}
