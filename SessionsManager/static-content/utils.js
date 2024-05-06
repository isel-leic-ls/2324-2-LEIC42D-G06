import {basicError} from "./components/basicError.js";

export const CONSTS = {
    BASE_API_URL: "http://localhost:9000/api",
    FIRST_GAME_ID: 100,
    FIRST_PLAYER_ID: 1000,
    FIRST_SESSION_ID: 10000,
    SKIP_DEFAULT: 0,
    LIMIT_DEFAULT: 5,
    HARDCODED_TOKEN : "Bearer 3ad7db4b-c5a9-42ee-9094-852f94c57cb7",
    HARDCODED_ID : 1000
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