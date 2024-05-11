import {basicError} from "./components/basicError.js";

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
        const errorContent = basicError(error.message)
        mainContent.replaceChildren(errorContent)
    }
}

export default CONSTS;