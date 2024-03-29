import { fetcher } from "../fetch.js";
import { filterQueryParameters } from "../uriparsers.js";

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