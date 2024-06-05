import { CONSTS } from '../utils.js';
import { DetailedError } from '../utils.js';


export class PlayerService {
    constructor(playerRepository) {
        this.playerRepository = playerRepository;
    }

    async playerDetailsRetrieval(pId) {
        const parsedPlayerId = parseInt(pId);
        if (isNaN(parsedPlayerId) || parsedPlayerId < CONSTS.FIRST_PLAYER_ID)
            throw new DetailedError("Invalid player ID", "");
        return await this.playerRepository.handlePlayerDetailsRequest(pId);
    }

    async playerIdRetrieval(token) {
        return await this.playerRepository.handlePlayerId(token);
    }

    async playerRegistration(name, email, password) {
        return await this.playerRepository.handleRegister(name, email, password);
    }

    async playerLogin(username, password) {
        return await this.playerRepository.handleLogin(username, password);
    }

    async playersRetrieval(name, skip, limit) {
        if(name === undefined) throw new DetailedError("Invalid name", "");

        if (isNaN(skip) || skip < 0)
            throw new DetailedError("Invalid skip", "");
        if (isNaN(limit) || limit < 1)
            throw new DetailedError("Invalid limit", "");
        const query = `name=${name}&skip=${skip}&limit=${limit}`;
        return await this.playerRepository.handlePlayersRetrievalRequest(query);
    }
}