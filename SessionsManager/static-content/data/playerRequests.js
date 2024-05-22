import { CONSTS } from "../utils.js";
import { DetailedError } from "../utils.js";


export class PlayerRepository {
    async handlePlayerDetailsRequest(pid) {
        const response = await fetch(CONSTS.BASE_API_URL + "/players/" + pid, {
            headers: {
                "Accept": "application/json",
            }
        });

        const jsonResp = await response.json();
        if (response.status === 200) return jsonResp;
        throw new DetailedError("Failed to retrieve player details", "Details: " + jsonResp.description);
    }

    async handlePlayerId(token) {
        const result = await fetch(CONSTS.BASE_API_URL + "/players/token/info", {
            headers: {
                "Accept": "application/json",
                "Authorization": token,
            },
        });

        const jsonResp = await result.json();
        if (result.status === 200) return jsonResp;
        throw new DetailedError("Failed to retrieve player details", "Details: " + jsonResp.description);
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

        const jsonResp = await response.json();
        if (response.status === 201) return;
        throw new DetailedError("Failed to register player", "Details: " + jsonResp.description);
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

        const jsonResp = await response.json();
        if (response.status === 200) return jsonResp;
        throw new DetailedError("Failed to login player", "Details: " + jsonResp.description);
    }

    async handlePlayersRetrievalRequest(query) {
        const response = await fetch(CONSTS.BASE_API_URL + "/players?" + query, {
            headers: {
                "Accept": "application/json",
            }
        });

        const jsonResp = await response.json();
        if (response.status === 200) return jsonResp;
        throw new DetailedError("Failed to retrieve players", "Details: " + jsonResp.description);
    }
}