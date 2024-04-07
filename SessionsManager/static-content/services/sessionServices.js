import { handleSessionsRetrievalRequest, handleSessionDetailsRequest } from "../data/sessionsRequests.js";


export async function sessionDetailsRetrieval(sid) {
    return await handleSessionDetailsRequest(sid);
}

export async function sessionsRetrieval(gid, date, state, pid, skip, limit) {
    let queryString = "";

    if (gid !== undefined && gid !== "null") {
        queryString += `gid=${gid}&`;
    }
    if (date !== undefined && date !== "null") {
        queryString += `date=${date}&`;
    }
    if (state !== undefined && state !== "null") {
        queryString += `state=${state}&`;
    }
    if (pid !== undefined && pid !== "null") {
        queryString += `pid=${pid}&`;
    }

    queryString += `skip=${skip}&limit=${limit}`;
    return await handleSessionsRetrievalRequest(queryString);
}

