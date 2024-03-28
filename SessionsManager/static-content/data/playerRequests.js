import { fetcher } from "../fetch.js";

export async function handlePlayerDetailsRequest() {
    const result = await fetcher("/players/1000", "GET", undefined); // hard-coded
    return result
}