export interface listaPendientes {
    id: number;
    idUsuario: number;
    nombreUsuario: string;
    correoUsuario: string;
    codigo: string;
    fechaSolicitud: string;
    fechaUso?: string;
    estado: number;
}
