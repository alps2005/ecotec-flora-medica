import { suscriptoresService } from "../lib/suscriptores";

document.addEventListener("DOMContentLoaded", () => {

    const formulario = document.querySelector("[data-suscripcion-form]");

    if (!(formulario instanceof HTMLFormElement)) {
        return;
    }

    formulario.addEventListener(
        "submit",
        async (e) => {

            e.preventDefault();

            const nombreField = formulario.querySelector("#full-name");
            const correoField = formulario.querySelector("#email");

            if (
                !(nombreField instanceof HTMLInputElement) ||
                !(correoField instanceof HTMLInputElement)
            ) {
                return;
            }

            const nombre = nombreField.value.trim();
            const correo = correoField.value.trim();

            if (!nombre || !correo) {

                alert("Complete todos los campos.");
                return;

            }

            try {

                await suscriptoresService.registrar({ nombre, correo });

                alert("¡Suscripción realizada correctamente!");

                formulario.reset();

            } catch (error) {

                console.error(error);
                alert(error instanceof Error ? error.message : "No fue posible registrar la suscripción.");

            }

        }

    );

});
