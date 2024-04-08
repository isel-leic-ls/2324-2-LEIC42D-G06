import { handlePlayerDetailsRequest, handlePlayerId } from '../data/playerRequests.js';


export async function playerDetailsRetrieval(pid) {
    return await handlePlayerDetailsRequest(pid);
}

export async function playerIdRetrieval(token) {
    return await handlePlayerId(token);
}