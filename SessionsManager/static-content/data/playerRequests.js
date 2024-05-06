import { CONSTS } from "../utils.js";

export class PlayerRepository {
    async handlePlayerDetailsRequest(pid) {
        const response = await fetch(CONSTS.BASE_API_URL + "/players/" + pid, {
            headers: {
                "Accept": "application/json",
            }
        });
        if (response.status === 200) {
            const player = await response.json();
            return player;
        }
        throw new Error("Failed to retrieve player details");
    }

    async handlePlayerId() {
        const result = await fetch("http://localhost:9000/api/players/token/info", {
            headers: {
                "Accept": "application/json",
                "Authorization": CONSTS.HARDCODED_TOKEN,
            },
        });
        if (result.status === 200) {
            const player = await result.json();
            return player;
        }
        throw new Error("Failed to retrieve player details");
    }
}
