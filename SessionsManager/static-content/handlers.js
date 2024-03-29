import { div } from "./tags.js"
import { handleGamesRetrievalRequest, handleGameDetailsRequest } from "./data/gamesRequests.js"
import { handlePlayerDetailsRequest } from "./data/playerRequests.js"
import { playerDetailsPage } from "./pages/playerPages.js"
import { gameDetailsPage, gameListPage, gamesSearchPage } from "./pages/gamesPages.js"
import { homePage } from "./pages/homePage.js"


/** Home */
function getHome(mainContent) {
    const pageContent = homePage();
    mainContent.replaceChildren(pageContent);
}

/** Games */
function getGamesSearch(mainContent) {
    const pageContent = gamesSearchPage();
    mainContent.replaceChildren(pageContent);
}

async function getGamesList(mainContent, path) {
    const result = await handleGamesRetrievalRequest(path);
    const pageContent = gameListPage(result.games);
    mainContent.replaceChildren(pageContent);
}

async function getGameDetails(mainContent, path) {
    const game = await handleGameDetailsRequest(path);
    const pageContent = gameDetailsPage(game);
    mainContent.replaceChildren(pageContent);
}

/** Sessions */
function getSessions(mainContent) {
    const element = div({}, "Sessions");
    mainContent.replaceChildren(element);
}

/** Players */
async function getPlayer(mainContent) {
    const player = await handlePlayerDetailsRequest();
    const pageContent = playerDetailsPage(player);
    mainContent.replaceChildren(pageContent);
}


export const handlers = {
    getHome,
    getGamesSearch,
    getSessions,
    getPlayer,
    getGamesList,
    getGameDetails
}

export default handlers