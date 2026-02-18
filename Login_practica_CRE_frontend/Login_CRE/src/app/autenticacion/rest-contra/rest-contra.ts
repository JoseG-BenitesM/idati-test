import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-rest-contra',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './rest-contra.html',
  styleUrl: './rest-contra.css',
})
export class RestContra {

  correo: string = '';
  mensajeExito: boolean = false;

   @ViewChild('btnLogin') btnLogin!: ElementRef<HTMLButtonElement>;
  
  constructor(private router: Router){}

  solicitar(){
    console.log('solicitud enviada: ', this.correo);
    this.mensajeExito = true;

    /*setTimeout(() => {
      this.router.navigate(['/login']);
    }, 1000);*/
  }

  irLogin() {
    this.router.navigate(['/login']);
  }

  ngAfterViewChecked() {
    if (this.mensajeExito && this.btnLogin) {
      this.btnLogin.nativeElement.focus();
    }
  }
}
