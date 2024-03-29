import { fetcher } from "../fetch.js";
import { filterQueryParameters, filterUriId } from "../uriparsers.js";


export async function handleGamesRetrievalRequest(path) {
    const { skip, limit, genres, developer } = filterQueryParameters(path);

    const games = await fetcher("/games/list?skip=" + skip + "&limit=" + limit, "POST", {
        genres: genres.includes(',') ? genres.split(",").map(g => g.trim()) : [genres],
        developer: developer
    });
        return games;
    }

export async function handleGameDetailsRequest(path) {
    const gid = filterUriId(path);
    const game = await fetcher("/games/id/" + gid, "GET", undefined);
    return game;
}