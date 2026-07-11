import { api } from "./api";

export interface SuscriptorDTO {
    nombre?: string;
    correo: string;
}

export const suscriptoresService = {

    registrar: async (
        datos: SuscriptorDTO
    ) => {

        return await api.post(
            "/api/suscriptores",
            datos
        );

    }

};