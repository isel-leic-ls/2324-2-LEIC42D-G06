import { fetcher } from "../fetch.js";
import {filterUriId} from "../uriparsers.js";


let token = "Bearer 3ad7db4b-c5a9-42ee-9094-852f94c57cb7" // hard-coded

export async function handlePlayerDetailsRequest(path) {
    const id = filterUriId(path);
    const result = await fetcher("/players/" + id, "GET", undefined);
    return result;
}

export async function handlePlayerId(){
    const result = await fetch("http://localhost:9000/api/players/token/info",{
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization": token
        },

    })
    return await result.json();
}