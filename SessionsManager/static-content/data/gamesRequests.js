import { fetcher } from "../fetch.js";
import { filterQueryParameters, filterUriId } from "../uriparsers.js";


export async function handleGamesRetrievalRequest(path) {
    const { skip, limit, genres, developer } = filterQueryParameters(path);

    const checkedGenres = genres == undefined || genres == "null" ? "" : genres;
    const checkedDeveloper = developer == undefined || developer == "null" ? "" : developer;

    const games = await fetcher("/games?genres=" + checkedGenres +
    "&developer=" + checkedDeveloper + "&skip=" + skip + "&limit=" + limit, "GET", undefined);
    return games;
}

export async function handleGameDetailsRequest(path) {
    const gid = filterUriId(path);
    const game = await fetcher("/games/id/" + gid, "GET", undefined);
    return game;
}