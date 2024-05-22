import { CONSTS } from "../utils.js";
import { DetailedError } from "../utils.js";

export class GameRepository {
    async handleGamesRetrievalRequest(query) {
        const response = await fetch(CONSTS.BASE_API_URL + "/games?" + query, {
            headers: {
                "Accept": "application/json",
            }
        });
        const jsonResp = await response.json();
        if (response.status === 200) {
            return jsonResp;
        }
        throw new DetailedError("Failed to retrieve games", "Details: " + jsonResp.description);
    }

    async handleGameDetailsRequest(gid) {
        const response = await fetch(CONSTS.BASE_API_URL + "/games/id/" + gid, {
            headers: {
                "Accept": "application/json",
            }
        });
        const jsonResp = await response.json();
        if (response.status === 200) {
            return jsonResp;
        }
        throw new DetailedError("Failed to retrieve game details", "Details: " + jsonResp.description);
    }

    async handleGamesRetrievalByNameRequest(query) {
        const response = await fetch(CONSTS.BASE_API_URL + "/games/list?" + query, {
            headers: {
                "Accept": "application/json",
            }
        });
        const jsonResp = await response.json();
        if (response.status === 200) {
            return jsonResp;
        }
        throw new DetailedError("Failed to retrieve games", "Details: " + jsonResp.description);
    }

    async handleGameCreationRequest(name, dev, genres, token) {
        const body = {
            name: name,
            developer: dev,
            genres: genres
        };

        const response = await fetch(CONSTS.BASE_API_URL + "/games", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "Authorization": token
            },
            body: JSON.stringify(body)
        });

        const jsonResp = await response.json();
        if (response.status === 201) {
            return jsonResp.gId;
        }
        throw new DetailedError("Failed to create game", "Details: " + jsonResp.description);
    }
}
