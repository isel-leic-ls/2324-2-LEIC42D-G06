import { fetcher } from "../fetch.js";
import { filterQueryParameters, filterUriId } from "../uriparsers.js";

export async function handleSessionsRetrievalRequest(path) {
    const { gid, date, state, pid, skip, limit } = filterQueryParameters(path);

    // Construct the query string
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

    // Remove trailing '&' if present
    if (queryString.endsWith('&')) {
        queryString = queryString.slice(0, -1);
    }

    const finalQueryString = queryString == "" ? "" : "?" + queryString

    const result = await fetcher("/sessions" + finalQueryString, "GET", undefined);

    return result;
}

export async function handleSessionDetailsRequest(path) {
    const sid = filterUriId(path);
    const result = await fetcher("/sessions/" + sid, "GET", undefined);
    return result
}