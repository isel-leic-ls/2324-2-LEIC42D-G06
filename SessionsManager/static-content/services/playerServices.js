import { CONSTS } from '../utils.js';

export class PlayerService {
    constructor(playerRepository) {
        this.playerRepository = playerRepository;
    }

    async playerDetailsRetrieval(pId) {
        const parsedPlayerId = parseInt(pId);
        if (isNaN(parsedPlayerId) || parsedPlayerId < CONSTS.FIRST_PLAYER_ID)
            throw new Error("Invalid player ID");
        return await this.playerRepository.handlePlayerDetailsRequest(pId);
    }

    async playerIdRetrieval(token) {
        return await this.playerRepository.handlePlayerId(token);
    }
}