import { CONSTS } from "../utils.js";
import { DetailedError } from "../utils.js";

export class SessionRepository {
    async handleSessionsRetrievalRequest(query) {
        const response = await fetch(CONSTS.BASE_API_URL + "/sessions?" + query, {
            headers: {
                "Accept": "application/json",
            }
        });
        const jsonResp = await response.json();
        if (response.status === 200) {
            return jsonResp;
        }
        throw new DetailedError("Failed to retrieve sessions", "Details: " + jsonResp.description);
    }

    async handleSessionDetailsRequest(sid) {
        const response = await fetch(CONSTS.BASE_API_URL + "/sessions/" + sid, {
            headers: {
                "Accept": "application/json",
            }
        });
        const jsonResp = await response.json();
        if (response.status === 200) {
            return jsonResp;
        }
        throw new DetailedError("Failed to retrieve session details", "Details: " + jsonResp.description);
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
        const jsonResp = await response.json();
        if (response.status === 201) {
            return jsonResp.sid;
        }
        throw new DetailedError("Failed to create session", "Details: " + jsonResp.description);
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
        const jsonResp = await response.json();
        throw new DetailedError("Failed to leave session " + sid, "Details: " + jsonResp.description);
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
        const jsonResp = await response.json();
        throw new DetailedError("Failed to update session", "Details: " + jsonResp.description);
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
        const jsonResp = await response.json();
        throw new DetailedError("Failed to delete session " + sid, "Details: " + jsonResp.description);
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
        const jsonResp = await response.json();
        throw new DetailedError("Failed to join session " + sid, "Details: " + jsonResp.description);
    }
}
