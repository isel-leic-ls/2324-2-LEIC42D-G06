import { handlePlayerDetailsRequest, handlePlayerId } from '../data/playerRequests.js';
import { CONSTS } from '../utils.js';


export async function playerDetailsRetrieval(pId) {
    const parsedPlayerId = parseInt(pId);
    if (parsedPlayerId == NaN || parsedPlayerId < CONSTS.FIRST_PLAYER_ID)
        throw new Error("Invalid player ID");
    return await handlePlayerDetailsRequest(pId);
}

export async function playerIdRetrieval(token) { //TODO: implement token validation
    //if (token == undefined || token == "" || token == null)
       //throw new Error("Invalid token");
    return await handlePlayerId(token);
}