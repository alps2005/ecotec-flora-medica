document.addEventListener("DOMContentLoaded", () => {

    const formulario = document.querySelector("form");

    formulario?.addEventListener(
        "submit",
        async (e) => {

            e.preventDefault();

            const nombre = document
                .getElementById("full-name")
                .value
                .trim();

            const correo = document
                .getElementById("email")
                .value
                .trim();

            if (!nombre || !correo) {

                alert("Complete todos los campos.");
                return;

            }

            try {

                const respuesta = await fetch(
                    "http://localhost:3000/api/suscriptores",
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            nombre,
                            correo
                        })
                    }
                );

                const resultado = await respuesta.json();

                console.log(resultado);

                if (!respuesta.ok) {

                    throw new Error(
                        resultado.message ||
                        "No fue posible registrar la suscripción."
                    );

                }

                alert("¡Suscripción realizada correctamente!");

                formulario.reset();

            } catch (error) {

                console.error(error);

                alert(error.message);

            }

        }

    );

});