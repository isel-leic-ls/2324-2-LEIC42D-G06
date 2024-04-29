import { handleGameDetailsRequest, handleGamesRetrievalRequest, handleGamesRetrievalByNameRequest } from "../data/gamesRequests.js";
import { CONSTS } from "../utils.js";


export async function gameDetailsRetrieval(gId) {
    const parsedGameId = parseInt(gId);
    if (isNaN(parsedGameId) || parsedGameId < CONSTS.FIRST_GAME_ID)
        throw new Error("Invalid game ID");
    return await handleGameDetailsRequest(gId);
}

export async function gamesRetrieval(skip, limit, genres, developer) {
    if (genres === undefined || genres === "") throw new Error("Invalid genres");
    if (developer === undefined || developer === "") throw new Error("Invalid developer");
    const checkedSkip = isNaN(skip) || skip < 0 ? CONSTS.SKIP_DEFAULT : skip;
    const checkedLimit = isNaN(limit) || limit < 1 ? CONSTS.LIMIT_DEFAULT : limit;

    const query = `genres=${genres}&developer=${developer}&skip=${checkedSkip}&limit=${checkedLimit}`;
    return await handleGamesRetrievalRequest(query);
}

export async function gamesByNameRetrieval(name) {
    if (name === undefined || name === "") throw new Error("Invalid name");
    return await handleGamesRetrievalByNameRequest(name);
}