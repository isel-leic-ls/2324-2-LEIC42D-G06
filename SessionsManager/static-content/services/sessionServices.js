import { handleSessionsRetrievalRequest, handleSessionDetailsRequest, handleSessionCreationRequest } from "../data/sessionsRequests.js";
import { CONSTS } from "../utils.js";
import { pattern} from "../pages/sessionsPages.js";

const states = ["OPEN", "CLOSED"];
const dateTimePattern = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/;

export async function sessionDetailsRetrieval(sId) {
    const parsedSessionId = parseInt(sId);
    if (isNaN(parsedSessionId) || parsedSessionId < CONSTS.FIRST_SESSION_ID)
        throw new Error("Invalid session ID");
    return await handleSessionDetailsRequest(sId);
}

export async function sessionsRetrieval(gid, date, state, pid, skip, limit) {
    const parsedGameId = parseInt(gid);
    if (gid !== undefined && (isNaN(parsedGameId) || parsedGameId < CONSTS.FIRST_GAME_ID))
        throw new Error("Invalid game ID");
    if (date !== "null" && date !== undefined && !pattern.test(date))
        throw new Error("Invalid date");
    if (state !== "null" && state !== undefined && !states.includes(state))
        throw new Error("Invalid state");
    const parsedPlayerId = parseInt(pid);
    if (pid !== undefined && (isNaN(parsedPlayerId) || parsedPlayerId < CONSTS.FIRST_PLAYER_ID))
        throw new Error("Invalid player ID");
    if (isNaN(skip) || skip < 0)
        throw new Error("Invalid skip");
    if (isNaN(limit) || limit < 1)
        throw new Error("Invalid limit");

    let queryString = "";

    if (gid !== undefined)
        queryString += `gid=${gid}&`;
    if (date !== undefined && date !== "null")
        queryString += `date=${date}&`;
    if (state !== undefined && state !== "null")
        queryString += `state=${state}&`;
    if (pid !== undefined)
        queryString += `pid=${pid}&`;

    queryString += `skip=${skip}&limit=${limit}`;

    return await handleSessionsRetrievalRequest(queryString);
}

export async function sessionCreation(gid, capacity, date) {
    const parsedGameId = parseInt(gid);
    if (isNaN(parsedGameId) || parsedGameId < CONSTS.FIRST_GAME_ID)
        throw new Error("Invalid game ID");
    const parsedCapacity = parseInt(capacity);
    if (isNaN(parsedCapacity) || parsedCapacity < 2)
        throw new Error("Invalid capacity");
    if (!dateTimePattern.test(date))
        throw new Error("Invalid date");
    return await handleSessionCreationRequest(gid, capacity, date);
}

