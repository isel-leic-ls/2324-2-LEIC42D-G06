import { homePage } from "./pages/homePage.js"
import { gamesSearchPage, gamesListPage, gameDetailsPage } from "./pages/gamesPages.js"
import { handleGamesRetrievalRequest, handleGameDetailsRequest } from "./data/gamesRequests.js"
import { handlePlayerDetailsRequest, handlePlayerId } from "./data/playerRequests.js"
import { sessionsSearchPage, sessionsListPage, sessionDetailsPage } from "./pages/sessionsPages.js"
import { playerDetailsPage } from "./pages/playerPages.js"
import { handleSessionsRetrievalRequest, handleSessionDetailsRequest } from "./data/sessionsRequests.js"
import { pagingButtons } from "./components/pagingButtons.js"


/** Home */
async function getHome(mainContent) {
    const player = await handlePlayerId()
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
    const buttons = pagingButtons(path,result.total)
    const pageContent = gamesListPage(result.games, buttons);
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

async function getSessionsList(mainContent, path) {
    const {sessions,total} = await handleSessionsRetrievalRequest(path);
    const buttons = pagingButtons(path,total)
    const pageContent = sessionsListPage(sessions, buttons);
    mainContent.replaceChildren(pageContent);
}

async function getSessionDetails(mainContent, path) {
    const result= await handleSessionDetailsRequest(path);
    const pageContent = sessionDetailsPage(result.session);
    mainContent.replaceChildren(pageContent);
}

/** Players */
async function getPlayer(mainContent, path) {
    const player = await handlePlayerDetailsRequest(path);
    const pageContent = playerDetailsPage(player);
    mainContent.replaceChildren(pageContent);
}

export const handlers = {
    getHome,
    getGamesSearch,
    getGamesList,
    getGameDetails,
    getSessionsSearch,
    getSessionsList,
    getSessionDetails,
    getPlayer
}

export default handlers