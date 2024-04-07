import { CONSTS } from "../utils.js";

export async function handleGamesRetrievalRequest(query) {
    const response = await fetch(CONSTS.BASE_API_URL + "/games?" + query, {
        headers: {
            "Accept": "application/json",
        }
    })
    if(response.status === 200) {
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
    if(response.status === 200) {
        const game = await response.json();
        return game;
    }
    throw new Error("Failed to retrieve game details");
}


