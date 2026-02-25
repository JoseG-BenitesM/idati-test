import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SolicitudService } from '../seguridad/servicios/solicitudes/solicitud-service';
import { validate } from '@angular/forms/signals';

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
  submitted: boolean = false;

  constructor (private router: Router, private SLTS: SolicitudService, private fb: FormBuilder ){}

  ngOnInit(): void {
    this.RcontraForm = this.fb.group({
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

    const { codigo, nuevaContrasena } = this.RcontraForm.value;
    console.log('Valores:', codigo, nuevaContrasena);

    this.SLTS.restablecer(codigo, nuevaContrasena).subscribe({
      next: (resp) => {
        console.log('Respuesta del backend:', resp);
        this.mensaje = resp.mensaje || "contraseña restablecida con exito";
        this.ModalCDC = true;
        this.RcontraForm.reset();
        this.submitted = false;
      },
      error: (err) => {
        console.error('Error al restablecer:', err);
        this.mensaje = err.error?.error || "Surgio un error al restablecer la contraseña";
        this.ModalCDC = true;
        this.submitted = false;
      }
    });

  }
}
