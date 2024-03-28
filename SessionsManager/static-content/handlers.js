import { div, a, ul, li, label, input, button } from "./tags.js"
import { handleGamesRetrievalRequest, handleGameDetailsRequest } from "./data/gameRequests.js"
import { handlePlayerDetailsRequest } from "./data/playerRequests.js"
import { playerDetailsPage } from "./pages/playerPages.js"
import { gameDetailsPage, gameListPage, gameSearchPage } from "./pages/gamePages.js"
import { homePage } from "./pages/homePage.js"

function getHome(mainContent) {
    const pageContent = homePage();
    mainContent.replaceChildren(pageContent);
}

function getGames(mainContent) {
    const pageContent = gameSearchPage();
    mainContent.replaceChildren(pageContent);
}

async function getGame(mainContent, path) {
    const game = await handleGameDetailsRequest(path);
    const pageContent = gameDetailsPage(game);
    mainContent.replaceChildren(pageContent);
}


function getSessions(mainContent) {
    const element = div({}, "Sessions");
    mainContent.replaceChildren(element);
}

async function getGamesList(mainContent, path) {
    const result = await handleGamesRetrievalRequest(path);
    const pageContent = gameListPage(result.games);
    mainContent.replaceChildren(pageContent);
}


async function getPlayer(mainContent) {
    const player = await handlePlayerDetailsRequest();
    const pageContent = playerDetailsPage(player);
    mainContent.replaceChildren(pageContent);
}


export const handlers = {
    getHome,
    getGames,
    getSessions,
    getPlayer,
    getGamesList,
    getGame
}

export default handlers