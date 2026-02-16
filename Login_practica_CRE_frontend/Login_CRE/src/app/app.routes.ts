import { Routes } from '@angular/router';
import { Login } from './autenticacion/login/login';
import { Usuarios } from './paginas/usuarios/usuarios';
import { InicioUsuario } from './paginas/inicio-usuario/inicio-usuario';
import { RestContra } from './autenticacion/rest-contra/rest-contra';

export const routes: Routes = [
    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: 'login', component: Login},
    {path: 'usuarios', component: Usuarios},
    {path: 'inicio-usuario', component: InicioUsuario},
    {path: 'restaurar-contraseña', component: RestContra}
];
