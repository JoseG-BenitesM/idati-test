import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SolicitudService } from '../seguridad/servicios/solicitudes/solicitud-service';

@Component({
  selector: 'app-rest-contra',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './rest-contra.html',
  styleUrl: './rest-contra.css',
})
export class RestContra implements OnInit {
  solicitudForm!: FormGroup;
  mostrarErrores: boolean = false;
  mensaje: string ='';
  mostrarModal: boolean = false;
  modalExito: boolean = false;
  submitted: boolean = false;

  @ViewChild('btnModal') btnModal?: ElementRef<HTMLButtonElement>;
  
  constructor(private router: Router, private SLTS: SolicitudService, private fb: FormBuilder ){}

  ngOnInit(): void {
    this.solicitudForm = this.fb.group({
      correo: ['', [Validators.required, Validators.email]]
    });
  }

  solicitar(): void {
    this.submitted = true;

    if (this.solicitudForm.invalid) {
      this.solicitudForm.markAllAsTouched();
      return;
    }

    const correo = this.solicitudForm.value.correo;

    this.SLTS.solictarRecuperacion(correo).subscribe({
      next: (e) =>{
        this.mensaje = e.mensaje;
        this.modalExito = true;
        this.solicitudForm.reset();
        this.submitted = false;
        this.mostrarModal = true;
        setTimeout(() => {this.btnModal?.nativeElement.focus();});
      },
      error: (err) => {
        if(err.status === 409){
          this.mensaje = err.error?.error || 'solicitud en revision';
        }else{
          this.mensaje = err.error?.error || 'error al ejecutar';
        }
        this.modalExito = false;
        this.solicitudForm.reset();
        this.submitted = false;
        this.mostrarModal = true;
        setTimeout(() => {this.btnModal?.nativeElement.focus();});
      }
    });

    /*setTimeout(() => {
      this.router.navigate(['/login']);
    }, 1000);*/
  }

  cerrarModal(){
    this.mostrarModal = false;
  }

  irLogin() {
    this.router.navigate(['/login']);
  }

  getEmailErrorMessage(): string {
    const control = this.solicitudForm.get('correo');
    if (control?.hasError('required')) return '¡Atención! El correo es obligatorio.';
    
    if (control?.hasError('email')) {return 'Por favor, ingresa un correo válido.'}

    return '';
  }
}
