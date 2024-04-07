import { handleGameDetailsRequest , handleGamesRetrievalRequest } from "../data/gamesRequests.js";

export async function gameDetailsRetrieval(gid) {
    return await handleGameDetailsRequest(gid);
}

export async function gamesRetrieval(skip, limit, genres, developer) {
    const checkedGenres = genres == undefined || genres == "null" ? "" : genres;
    const checkedDeveloper = developer == undefined || developer == "null" ? "" : developer;

    const query = `genres=${checkedGenres}&developer=${checkedDeveloper}&skip=${skip}&limit=${limit}`;
    return await handleGamesRetrievalRequest(query);
}