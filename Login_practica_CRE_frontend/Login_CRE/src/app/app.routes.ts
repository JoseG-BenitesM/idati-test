import { Routes } from '@angular/router';
import { Login } from './autenticacion/login/login';
import { UsuariosComponent } from './paginas/usuarios/usuarios';
import { InicioUsuario } from './paginas/inicio-usuario/inicio-usuario';
import { RestContra } from './autenticacion/rest-contra/rest-contra';
import { ListaPendientes } from './paginas/lista-pendientes/lista-pendientes';
import { NuevaContrasena } from './autenticacion/nueva-contrasena/nueva-contrasena';

export const routes: Routes = [
    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: 'login', component: Login},
    {path: 'usuarios', component: UsuariosComponent},
    {path: 'inicio-usuario', component: InicioUsuario},
    {path: 'restaurar-contraseña', component: RestContra},
    {path: 'pendientes', component: ListaPendientes},
    {path: 'Restablecer', component: NuevaContrasena}
];
