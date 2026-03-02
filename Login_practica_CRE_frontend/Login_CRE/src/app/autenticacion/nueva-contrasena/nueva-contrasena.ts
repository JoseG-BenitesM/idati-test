import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SolicitudService } from '../seguridad/servicios/solicitudes/solicitud-service';

@Component({
  selector: 'app-nueva-contrasena',
  imports: [ReactiveFormsModule],
  templateUrl: './nueva-contrasena.html',
  styleUrl: './nueva-contrasena.css',
})
export class NuevaContrasena implements OnInit {
  RcontraForm!: FormGroup;
  mensaje: string = '';
  ModalCDC: boolean = false;
  modalExito: boolean = false;
  submitted: boolean = false;
  @ViewChild('btnModal') btnModal?: ElementRef<HTMLButtonElement>;

  constructor (private router: Router, private SLTS: SolicitudService, private fb: FormBuilder ){}

  ngOnInit(): void {
    this.RcontraForm = this.fb.group({
      correo: ['', [Validators.required]],
      codigo: ['', [Validators.required]],
      nuevaContrasena: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  cambiar_contra(): void{
    this.submitted = true;
    console.log('Formulario enviado');

    if (this.RcontraForm.invalid) {
      console.log('Formulario inválido', this.RcontraForm.errors);
      this.RcontraForm.markAllAsTouched();
      return;
    }

    const { correo, codigo, nuevaContrasena } = this.RcontraForm.value;
    console.log('Valores:', correo, codigo, nuevaContrasena);

    this.SLTS.restablecer(correo, codigo, nuevaContrasena).subscribe({
      next: (resp) => {
        console.log('Respuesta del backend:', resp);
        this.mensaje = resp.mensaje || "contraseña restablecida con exito";
        this.modalExito = true;
        this.RcontraForm.reset();
        this.ModalCDC = true;
        this.submitted = false;
        setTimeout(() => {this.btnModal?.nativeElement.focus();});
      },
      error: (err) => {
        console.error('Error al restablecer:', err);
        this.mensaje = err.error?.error || "Surgio un error al restablecer la contraseña";
        this.modalExito = false;
        this.RcontraForm.reset();
        this.ModalCDC = true;
        this.submitted = false;
        setTimeout(() => {this.btnModal?.nativeElement.focus();});
      }
    });
  }

  cerrarModal(){
    this.ModalCDC = false;
  }

  irLogin() {
    this.router.navigate(['/login']);
  }
}
