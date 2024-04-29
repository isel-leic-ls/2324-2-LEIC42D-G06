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

export async function handleSessionLeaveRequest(sid) {
    const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid + "/players", {
        method: "DELETE",
        headers: {
            "Accept": "application/json",
            "Authorization" : token
        }
    });

    if (response.status === 204) {
        return;
    }
    throw new Error("Failed to leave session " + sid);

}

export async function handleSessionUpdateRequest(sid, capacity, date) {

    const body = {
        capacity: capacity,
        date: date,
    };

    const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization" : token
        },
        body: JSON.stringify(body)
    })

    if (response.status === 204) {
        return;
    }
    throw new Error("Failed to update session");
}

export async function handleSessionDeleteRequest(sid) {
    const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid, {
        method: "DELETE",
        headers: {
            "Accept": "application/json",
            "Authorization" : token
        }
    });

    if (response.status === 204) {
        return;
    }
    throw new Error("Failed to delete session " + sid);
}

export async function handleSessionJoinRequest(sid) {
    const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid + "/players", {
        method: "PUT",
        headers: {
            "Accept": "application/json",
            "Authorization" : token
        }
    });

    if (response.status === 204) {
        return;
    }
    throw new Error("Failed to join session " + sid);
}