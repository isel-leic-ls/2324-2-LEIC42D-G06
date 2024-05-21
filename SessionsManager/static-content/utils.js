import {basicError} from "./components/basicError.js";

// BASE_API_URL = "http://localhost:9000/api"

// BASE_API_URL: "https://img-ls-2324-2-42d-g06.onrender.com/api"
export const CONSTS = {
    BASE_API_URL: "http://localhost:9000/api",
    FIRST_GAME_ID: 100,
    FIRST_PLAYER_ID: 1000,
    FIRST_SESSION_ID: 10000,
    SKIP_DEFAULT: 0,
    LIMIT_DEFAULT: 5,
}

export async function safeCall(mainContent, block){
    try{
        await block()
    }catch(error){
        console.log(error)
        const errorContent = basicError(error.message, error.details)
        mainContent.replaceChildren(errorContent)
    }
}

export class DetailedError extends Error {
    constructor(message, details){
        super(message)
        this.details = details
    }
}

export default CONSTS;