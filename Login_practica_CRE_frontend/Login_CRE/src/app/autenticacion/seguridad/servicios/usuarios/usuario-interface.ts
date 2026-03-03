export interface UsuarioRol {
  id: number;
  fechaAsignacion: string;
}

export interface Usuario {
  id: number;
  usuarioNombre: string;
  correoElectronico: string;
  contrasena: string;
  estadoUsuario: number;        
  intentosFallidos: number;
  fechaUltimoIntento?: string | null;
  fechaAlta?: string;
  fechaBaja?: string | null;
  usuarioRoles: UsuarioRol[];
  activo: boolean;              
}