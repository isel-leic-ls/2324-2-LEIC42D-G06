import { CONSTS } from "../utils.js";

export async function handleSessionsRetrievalRequest(query) {
    const response = await fetch(CONSTS.BASE_API_URL + "/sessions?" + query, {
        headers: {
            "Accept": "application/json",
        }
    })
    if(response.status === 200) {
        const sessions = await response.json();
        return sessions;
    }
    throw new Error("Failed to retrieve sessions");
}

export async function handleSessionDetailsRequest(sid) {
    const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid, {
        headers: {
            "Accept": "application/json",
        }
    })

    if(response.status === 200) {
        const session = await response.json();
        return session;
    }
    throw new Error("Failed to retrieve session details");
}