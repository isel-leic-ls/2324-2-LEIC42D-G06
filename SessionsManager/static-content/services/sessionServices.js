import { CONSTS } from "../utils.js";
import { pattern } from "../pages/sessionsPages.js";
import { DetailedError } from "../utils.js";

const states = ["OPEN", "CLOSED"];
const dateTimePattern = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/;

export class SessionService {
    constructor(sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    async sessionDetailsRetrieval(sId) {
        const parsedSessionId = parseInt(sId);
        if (isNaN(parsedSessionId) || parsedSessionId < CONSTS.FIRST_SESSION_ID)
            throw new DetailedError("Invalid session ID", "");
        return await this.sessionRepository.handleSessionDetailsRequest(sId);
    }

    async sessionsRetrieval(gid, date, state, pid, skip, limit) {
        const parsedGameId = parseInt(gid);
        if (gid !== undefined && (isNaN(parsedGameId) || parsedGameId < CONSTS.FIRST_GAME_ID))
            throw new DetailedError("Invalid game ID", "");
        if (date !== "null" && date !== undefined && !pattern.test(date))
            throw new DetailedError("Invalid date", "");
        if (state !== "null" && state !== undefined && !states.includes(state))
            throw new DetailedError("Invalid state", "");
        const parsedPlayerId = parseInt(pid);
        if (pid !== undefined && (isNaN(parsedPlayerId) || parsedPlayerId < CONSTS.FIRST_PLAYER_ID))
            throw new DetailedError("Invalid player ID", "");
        if (isNaN(skip) || skip < 0)
            throw new DetailedError("Invalid skip", "");
        if (isNaN(limit) || limit < 1)
            throw new DetailedError("Invalid limit", "");

        let queryString = "";

        if (gid !== undefined)
            queryString += `gid=${gid}&`;
        if (date !== undefined && date !== "null")
            queryString += `date=${date}&`;
        if (state !== undefined && state !== "null")
            queryString += `state=${state}&`;
        if (pid !== undefined)
            queryString += `pid=${pid}&`;

        queryString += `skip=${skip}&limit=${limit}`;

        return await this.sessionRepository.handleSessionsRetrievalRequest(queryString);
    }

    async sessionCreation(gid, capacity, date, token) {
        const parsedGameId = parseInt(gid);
        if (isNaN(parsedGameId) || parsedGameId < CONSTS.FIRST_GAME_ID)
            throw new DetailedError("Invalid game ID", "");
        const parsedCapacity = parseInt(capacity);
        if (isNaN(parsedCapacity) || parsedCapacity < 2)
            throw new DetailedError("Invalid capacity", "");
        if (!dateTimePattern.test(date))
            throw new DetailedError("Invalid date", "");
        return await this.sessionRepository.handleSessionCreationRequest(gid, capacity, date, token);
    }

    async sessionLeave(sid, token) {
        return await this.sessionRepository.handleSessionLeaveRequest(sid, token);
    }

    async sessionUpdate(sid, capacity, date, token) {
        if (isNaN(capacity) || capacity < 2)
            throw new DetailedError("Invalid capacity", "");
        if (!dateTimePattern.test(date))
            throw new DetailedError("Invalid date", "");
        return await this.sessionRepository.handleSessionUpdateRequest(sid, capacity, date, token);
    }

    async sessionDelete(sid, token) {
        return await this.sessionRepository.handleSessionDeleteRequest(sid, token);
    }

    async sessionJoin(sid, token) {
        return await this.sessionRepository.handleSessionJoinRequest(sid, token);
    }
}
