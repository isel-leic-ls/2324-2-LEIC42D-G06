import { CONSTS } from "../utils.js";

const token = "Bearer 3ad7db4b-c5a9-42ee-9094-852f94c57cb7";

export async function handleSessionsRetrievalRequest(query) {
    const response = await fetch(CONSTS.BASE_API_URL + "/sessions?" + query, {
        headers: {
            "Accept": "application/json",
        }
    })
    if (response.status === 200) {
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

    if (response.status === 200) {
        const session = await response.json();
        return session;
    }
    throw new Error("Failed to retrieve session details");
}

export async function handleSessionCreationRequest(gid, capacity, date) {

    const body = {
        gid: gid,
        capacity: capacity,
        date: date,
        // Assuming pid needs to be inserted into a list
    };

    const response = await fetch(CONSTS.BASE_API_URL + "/sessions", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization" : token
        },
        body: JSON.stringify(body)
    })

    if (response.status === 201) {
        const model = await response.json();
        return model.sid;
    }
    throw new Error("Failed to create session");
}