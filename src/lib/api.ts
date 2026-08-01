const API_URL =
    import.meta.env.PUBLIC_API_URL ?? "";

export const api = {

    get: async (
        endpoint: string,
        { timeoutMs = 5000 }: { timeoutMs?: number } = {}
    ) => {

        const controller = new AbortController();
        const timer = setTimeout(
            () => controller.abort(),
            timeoutMs
        );

        try {

            const response =
                await fetch(

                    `${API_URL}${endpoint}`,

                    {

                        method: "GET",

                        headers: {

                            "Content-Type":
                                "application/json"

                        },

                        signal: controller.signal

                    }

                );

            const data =
                await response.json();

            if (!response.ok) {

                throw new Error(

                    data?.message ||

                    "Error en la petición"

                );

            }

            return data;

        } finally {

            clearTimeout(timer);

        }

    },

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