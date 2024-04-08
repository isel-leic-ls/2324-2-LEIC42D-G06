import { CONSTS } from "../utils.js";


const token = "Bearer 3ad7db4b-c5a9-42ee-9094-852f94c57cb7";

export async function handlePlayerDetailsRequest(pid) {
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

export async function handlePlayerId() { //TODO: hardcoded token, change this
    const result = await fetch("http://localhost:9000/api/players/token/info", {
        headers: {
            "Accept": "application/json",
            "Authorization": token
        },

    })
    if (result.status === 200) {
        const player = await result.json();
        return player;
    }
    throw new Error("Failed to retrieve player details");
}