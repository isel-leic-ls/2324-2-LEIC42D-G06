import { fetcher } from "../fetch.js";
import { filterQueryParameters, filterUriId } from "../uriparsers.js";

export async function handleSessionsRetrievalRequest(path) {
    const { gid, date, state, pid, skip, limit } = filterQueryParameters(path);

    const result = await fetcher("/sessions/list?skip=" + skip + "&limit=" + limit, "POST", {
        gid : gid == undefined || gid == "null" ? null : gid,
        date : date == undefined || date == "null" ? null: date,
        state : state == undefined || state == "null" ? null : state,
        pid : pid == undefined || pid == "null" ? null : pid
    });
    return result;
}

export async function handleSessionDetailsRequest(path) {
    const sid = filterUriId(path);
    const result = await fetcher("/sessions/" + sid, "GET", undefined);
    return result
}