import { CONSTS } from "../utils.js";

export class PlayerRepository {
    async handlePlayerDetailsRequest(pid) {
        const response = await fetch(CONSTS.BASE_API_URL + "/players/" + pid, {
            headers: {
                "Accept": "application/json",
            }
        });
        if (response.status === 200) {
            const player = await response.json();
            return player;
        }
        throw new Error("Failed to retrieve player details");
    }

    async handlePlayerId(token) {
        const result = await fetch(CONSTS.BASE_API_URL + "/players/token/info", {
            headers: {
                "Accept": "application/json",
                "Authorization": token,
            },
        });
        if (result.status === 200) {
            const player = await result.json();
            return player;
        }
        throw new Error("Failed to retrieve player details");
    }

    async handleRegister(name, email, password) {
        const body = {
            name: name,
            email: email,
            password: password
        };

        const response = await fetch(CONSTS.BASE_API_URL + "/players", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
            },
            body: JSON.stringify(body)
        });

        if (response.status === 201) {
            //const model = await response.json();
            //return model.pid;
            return;
        }
        throw new Error("Failed to register player");
    }

    async handleLogin(username, password) {
        const body = {
            name: username,
            password: password
        };

        const response = await fetch(CONSTS.BASE_API_URL + "/players/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
            },
            body: JSON.stringify(body)
        });

        if (response.status === 200) {
            const model = await response.json();
            return model;
        }
        throw new Error("Failed to login player");
    }
}
