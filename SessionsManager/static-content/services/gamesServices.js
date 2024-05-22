import { CONSTS } from "../utils.js";
import { DetailedError } from "../utils.js";


export class GameService {
    constructor(gameRepository) {
        this.gameRepository = gameRepository;
    }

    async gameDetailsRetrieval(gId) {
        const parsedGameId = parseInt(gId);
        if (isNaN(parsedGameId) || parsedGameId < CONSTS.FIRST_GAME_ID)
            throw new DetailedError("Invalid game ID", "");
        return await this.gameRepository.handleGameDetailsRequest(gId);
    }

    async gamesRetrieval(skip, limit, genres, developer) {
        if (genres === undefined) throw new DetailedError("Invalid genres", "");
        if (developer === undefined) throw new DetailedError("Invalid developer", "");
        
        const checkedSkip = isNaN(skip) || skip < 0 ? CONSTS.SKIP_DEFAULT : skip;
        const checkedLimit = isNaN(limit) || limit < 1 ? CONSTS.LIMIT_DEFAULT : limit;

        const query = `genres=${genres}&developer=${developer}&skip=${checkedSkip}&limit=${checkedLimit}`;
        return await this.gameRepository.handleGamesRetrievalRequest(query);
    }

    async gamesByNameRetrieval(gname, skip, limit) {
        if (gname === undefined) throw new DetailedError("Invalid name", "");

        const checkedSkip = isNaN(skip) || skip < 0 ? CONSTS.SKIP_DEFAULT : skip;
        const checkedLimit = isNaN(limit) || limit < 1 ? CONSTS.LIMIT_DEFAULT : limit;
        const query = `gname=${gname}&skip=${checkedSkip}&limit=${checkedLimit}`;
        return await this.gameRepository.handleGamesRetrievalByNameRequest(query);
    }

    async gameCreation(name, dev, genres, token) {
        if (name === undefined || name === "") throw new DetailedError("Invalid name", "");
        if (dev === undefined || dev === "") throw new DetailedError("Invalid developer", "");
        if (genres === undefined || genres === "") throw new DetailedError("Invalid genres", "");

        const genreArray = genres.split(',');
        return await this.gameRepository.handleGameCreationRequest(name, dev, genreArray, token);
    }
}