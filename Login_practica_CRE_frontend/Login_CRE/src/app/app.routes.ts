import { Routes } from '@angular/router';
import { Login } from './autenticacion/login/login';
import { UsuariosComponent } from './paginas/usuarios/usuarios';
import { InicioUsuario } from './paginas/inicio-usuario/inicio-usuario';
import { RestContra } from './autenticacion/rest-contra/rest-contra';
import { ListaPendientes } from './paginas/lista-pendientes/lista-pendientes';
import { NuevaContrasena } from './autenticacion/nueva-contrasena/nueva-contrasena';
import { InicioAdmin } from './paginas/inicio-admin/inicio-admin';
import { sPTRGuard } from './autenticacion/sptr-guard';

export const routes: Routes = [
    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: 'login', component: Login},
    {path: 'inicio-usuario', component: InicioUsuario, canActivate: [sPTRGuard]},
    {path: 'restaurar-contraseña', component: RestContra},
    {path: 'Restablecer', component: NuevaContrasena},

    {path: 'inicio-admin', component: InicioAdmin, canActivate: [sPTRGuard], 
        data: { requiresAdmin: true }, children: [
            {path: 'usuarios', component: UsuariosComponent},
            {path: 'pendientes', component: ListaPendientes},
            { path: '', redirectTo: 'usuarios', pathMatch: 'full' }
        ]
    }
];
