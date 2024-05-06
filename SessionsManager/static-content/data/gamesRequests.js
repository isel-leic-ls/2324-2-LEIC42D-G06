import { CONSTS } from "../utils.js";

export class GameRepository {
    async handleGamesRetrievalRequest(query) {
        const response = await fetch(CONSTS.BASE_API_URL + "/games?" + query, {
            headers: {
                "Accept": "application/json",
            }
        });
        if (response.status === 200) {
            const games = await response.json();
            return games;
        }
        throw new Error("Failed to retrieve games");
    }

    async handleGameDetailsRequest(gid) {
        const response = await fetch(CONSTS.BASE_API_URL + "/games/id/" + gid, {
            headers: {
                "Accept": "application/json",
            }
        });
        if (response.status === 200) {
            const game = await response.json();
            return game;
        }
        throw new Error("Failed to retrieve game details");
    }

    async handleGamesRetrievalByNameRequest(query) {
        const response = await fetch(CONSTS.BASE_API_URL + "/games/list?" + query, {
            headers: {
                "Accept": "application/json",
            }
        });
        if (response.status === 200) {
            const games = await response.json();
            return games;
        }
        throw new Error("Failed to retrieve games");
    }

    async handleGameCreationRequest(name, dev, genres) {
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
                "Authorization": CONSTS.HARDCODED_TOKEN
            },
            body: JSON.stringify(body)
        });

        if (response.status === 201) {
            const model = await response.json();
            return model.gId;
        }
        throw new Error("Failed to create game");
    }
}
