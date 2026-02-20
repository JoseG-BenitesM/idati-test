import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgModel, FormsModule, NgForm } from '@angular/forms';
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
  mostrarErrores: boolean = false;

  @ViewChild('btnLogin') btnLogin!: ElementRef<HTMLButtonElement>;
  
  constructor(private router: Router){}

  solicitar(form: NgForm){
    this.mostrarErrores = true;

    if (form.invalid) {
      return;
    }

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

  getEmailErrorMessage(input: NgModel): string {

    if (input.errors?.['required']) return '¡Atención! El correo es obligatorio.';
    
    if (input.errors?.['email']) {return 'Por favor, ingresa un correo válido.'}

    return '';
  }
}
