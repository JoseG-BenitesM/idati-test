import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Proteccion {
  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  getUserRole(): string | null {
    return sessionStorage.getItem('rol');
  }

  getUser(): string | null {
    return sessionStorage.getItem('usuario');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    sessionStorage.clear();
  }
}
