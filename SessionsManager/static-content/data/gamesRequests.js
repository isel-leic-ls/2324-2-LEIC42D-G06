import { CONSTS } from "../utils.js";

const token = "Bearer 3ad7db4b-c5a9-42ee-9094-852f94c57cb7";

export async function handleGamesRetrievalRequest(query) {
    const response = await fetch(CONSTS.BASE_API_URL + "/games?" + query, {
        headers: {
            "Accept": "application/json",
        }
    })
    if (response.status === 200) {
        const games = await response.json();
        return games;
    }
    throw new Error("Failed to retrieve games");
}

export async function handleGameDetailsRequest(gid) {
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

export async function handleGamesRetrievalByNameRequest(query) {
    const response = await fetch(CONSTS.BASE_API_URL + "/games/list?"+ query, {
        headers: {
            "Accept": "application/json",
        }
    })
    if (response.status === 200) {
        const games = await response.json();
        return games;
    }
    throw new Error("Failed to retrieve games");
}

export async function handleGameCreationRequest(name, dev, genres) {

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
            "Authorization" : token
        },
        body: JSON.stringify(body)
    })

    if (response.status === 201) {
        const model = await response.json();
        return model.gId;
    }
    throw new Error("Failed to create game");
}