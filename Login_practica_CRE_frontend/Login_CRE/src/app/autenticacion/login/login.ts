import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
  loginForm!: FormGroup;
  showPassword: boolean = false;
  submitted: boolean = false;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.loginForm.valid) {
      console.log('Datos válidos:', this.loginForm.value);
    } else {
      this.loginForm.markAllAsTouched();
    }
  }

  getEmailErrorMessage(): string {
    const control = this.loginForm.get('email');
    const valor = control?.value || '';

    if (control?.hasError('required')) return '¡Atención! El correo es obligatorio.';
    
    // Validación de estructura básica
    if (!valor.includes('@')) return 'Al correo le falta el símbolo "@".';
    
    const partes = valor.split('@');
    if (!partes[1]) return 'Falta el dominio después del "@" (ej: empresa.com).';
    
    if (!partes[1].includes('.')) return 'El dominio debe incluir una extensión (ej: .com, .net).';

    return 'Por favor, ingresa un correo válido.';
  }
}