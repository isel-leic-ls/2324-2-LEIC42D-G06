import { homePage } from "./pages/homePage.js"
import { gamesSearchPage, gamesListPage, gameDetailsPage } from "./pages/gamesPages.js"
import { handleGamesRetrievalRequest, handleGameDetailsRequest } from "./data/gamesRequests.js"
import { handlePlayerDetailsRequest } from "./data/playerRequests.js"
import { sessionsSearchPage } from "./pages/sessionsPages.js"
import { playerDetailsPage } from "./pages/playerPages.js"


/** Home */
function getHome(mainContent) {
    const player = handlePlayerId()
    const pageContent = homePage(player);
    mainContent.replaceChildren(pageContent);
}

/** Games */
function getGamesSearch(mainContent) {
    const pageContent = gamesSearchPage();
    mainContent.replaceChildren(pageContent);
}

async function getGamesList(mainContent, path) {
    const result = await handleGamesRetrievalRequest(path);
    const pageContent = gamesListPage(result.games);
    mainContent.replaceChildren(pageContent);
}

async function getGameDetails(mainContent, path) {
    const game = await handleGameDetailsRequest(path);
    const pageContent = gameDetailsPage(game);
    mainContent.replaceChildren(pageContent);
}

/** Sessions */
function getSessionsSearch(mainContent) {
    const pageContent = sessionsSearchPage();
    mainContent.replaceChildren(pageContent);
}


async function getPlayer(mainContent) {
    const player = await handlePlayerDetailsRequest();
    const pageContent = playerDetailsPage(player);
    mainContent.replaceChildren(pageContent);
}

export const handlers = {
    getHome,
    getGamesSearch,
    getGamesList,
    getGameDetails,
    getSessionsSearch,
    getPlayer
}

export default handlers