import { CONSTS } from "../utils.js";

export class SessionRepository {
    async handleSessionsRetrievalRequest(query) {
        const response = await fetch(CONSTS.BASE_API_URL + "/sessions?" + query, {
            headers: {
                "Accept": "application/json",
            }
        });
        if (response.status === 200) {
            const sessions = await response.json();
            return sessions;
        }
        throw new Error("Failed to retrieve sessions");
    }

    async handleSessionDetailsRequest(sid) {
        const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid, {
            headers: {
                "Accept": "application/json",
            }
        });
        if (response.status === 200) {
            const session = await response.json();
            return session;
        }
        throw new Error("Failed to retrieve session details");
    }

    async handleSessionCreationRequest(gid, capacity, date, token) {
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
                "Authorization": token
            },
            body: JSON.stringify(body)
        });

        if (response.status === 201) {
            const model = await response.json();
            return model.sid;
        }
        throw new Error("Failed to create session");
    }

    async handleSessionLeaveRequest(sid, token) {
        const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid + "/players", {
            method: "DELETE",
            headers: {
                "Accept": "application/json",
                "Authorization": token
            }
        });

        if (response.status === 204) {
            return;
        }
        throw new Error("Failed to leave session " + sid);
    }

    async handleSessionUpdateRequest(sid, capacity, date, token) {
        const body = {
            capacity: capacity,
            date: date,
        };

        const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "Authorization": token
            },
            body: JSON.stringify(body)
        });

        if (response.status === 204) {
            return;
        }
        throw new Error("Failed to update session");
    }

    async handleSessionDeleteRequest(sid, token) {
        const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid, {
            method: "DELETE",
            headers: {
                "Accept": "application/json",
                "Authorization": token
            }
        });

        if (response.status === 204) {
            return;
        }
        throw new Error("Failed to delete session " + sid);
    }

    async handleSessionJoinRequest(sid, token) {
        const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid + "/players", {
            method: "PUT",
            headers: {
                "Accept": "application/json",
                "Authorization": token
            }
        });

        if (response.status === 204) {
            return;
        }
        throw new Error("Failed to join session " + sid);
    }
}
