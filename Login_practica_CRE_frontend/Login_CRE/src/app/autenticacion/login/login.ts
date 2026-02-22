import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { LoginAuthService } from '../seguridad/servicios/login_auth/login-auth-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
  loginForm!: FormGroup;
  showPassword: boolean = false;
  submitted: boolean = false;

  constructor(private fb: FormBuilder, private router: Router, private LGSV: LoginAuthService) {}

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
    
    if (this.loginForm.valid){
      const { email, password } = this.loginForm.value;

      this.LGSV.login({
        correoElectronico: email,
        contrasena: password
      }).subscribe({
        next: (response/*quitar despues*/) => {
          //console.log('Datos válidos:', this.loginForm.value);
          console.log(response);/*quitar despues*/
          this.router.navigate(['/usuarios'])
        },
        error: () => {
          alert('Credenciales incorrectas');
          this.loginForm.reset();
          this.submitted = false;
        }
      });
    } else {
      this.loginForm.markAllAsTouched();
    }

  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  getUsername(): string | null {
    return sessionStorage.getItem('usuario');
  }

  getRoles(): string[] {
    const roles = sessionStorage.getItem('roles');
    return roles ? JSON.parse(roles) : [];
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    sessionStorage.clear();
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


