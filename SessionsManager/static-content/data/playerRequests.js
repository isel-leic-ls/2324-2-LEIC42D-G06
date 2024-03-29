import { fetcher } from "../fetch.js";
import {filterUriId} from "../uriparsers";

export async function handlePlayerDetailsRequest(path) {
    const id = filterUriId(path);
    const result = await fetcher("/players/" + id, "GET", undefined); // hard-coded
    return result;
}

export async function handlePlayerId(){
    const result = await fetch("http://localhost:9000/api/players/token",{
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization": "Bearer 4aef5b7b-1693-4e49-a778-634360a3e537" //TOKEN HARDCODED TODO()
        },

    })
    return result
}