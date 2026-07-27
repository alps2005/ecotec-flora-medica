const API_URL =
    import.meta.env.PUBLIC_API_URL ?? "";

export const api = {

    post: async (
        endpoint: string,
        body: any
    ) => {

        const response =
            await fetch(

                `${API_URL}${endpoint}`,

                {

                    method: "POST",

                    headers: {

                        "Content-Type":
                            "application/json"

                    },

                    body: JSON.stringify(
                        body
                    )

                }

            );

        const data =
            await response.json();

        if (!response.ok) {

            throw new Error(

                data.message ||

                "Error en la petición"

            );

        }

        return data;

    }

};