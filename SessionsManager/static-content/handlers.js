import { homePage } from "./pages/homePage.js"
import { gamesSearchPage, gamesListPage, gameDetailsPage } from "./pages/gamesPages.js"
import { sessionsSearchPage, sessionsListPage, sessionDetailsPage } from "./pages/sessionsPages.js"
import { playerDetailsPage } from "./pages/playerPages.js"
import { pagingButtons } from "./components/pagingButtons.js"
import { basicError } from "./components/basicError.js"
import { filterQueryParameters, filterUriId } from "./uriparsers.js"
import { sessionsRetrieval, sessionDetailsRetrieval } from "./services/sessionServices.js"
import { gamesRetrieval, gameDetailsRetrieval } from "./services/gamesServices.js"
import { playerDetailsRetrieval, playerIdRetrieval } from "./services/playerServices.js"


/** Home */
async function getHome(mainContent) {
    try {
        const player = await playerIdRetrieval()
        const pageContent = homePage(player);
        mainContent.replaceChildren(pageContent);
    } catch (error) {
        console.error(error);
        const errorContent = basicError(error.message);
        mainContent.replaceChildren(errorContent);
    }
}

/** Games */
function getGamesSearch(mainContent) {
    const pageContent = gamesSearchPage();
    mainContent.replaceChildren(pageContent);
}

async function getGamesList(mainContent, path) {
    try {
        const { skip, limit, genres, developer } = filterQueryParameters(path);
        const { games, total } = await gamesRetrieval(skip, limit, genres, developer);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = gamesListPage(games, buttons);
        mainContent.replaceChildren(pageContent);
    }
    catch (error) {
        console.error(error);
        const errorContent = basicError(error.message);
        mainContent.replaceChildren(errorContent);
    }
}

async function getGameDetails(mainContent, path) {
    try {
        const gid = filterUriId(path);
        const game = await gameDetailsRetrieval(gid);
        const pageContent = gameDetailsPage(game);
        mainContent.replaceChildren(pageContent);
    }
    catch (error) {
        console.error(error);
        const errorContent = basicError(error.message);
        mainContent.replaceChildren(errorContent);
    }

}

/** Sessions */
function getSessionsSearch(mainContent) {
    const pageContent = sessionsSearchPage();
    mainContent.replaceChildren(pageContent);
}

async function getSessionsList(mainContent, path) {
    try {
        const { gid, date, state, pid, skip, limit } = filterQueryParameters(path);
        const { sessions, total } = await sessionsRetrieval(gid, date, state, pid, skip, limit);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = sessionsListPage(sessions, buttons);
        mainContent.replaceChildren(pageContent);
    } catch (error) {
        console.error(error);
        const errorContent = basicError(error.message);
        mainContent.replaceChildren(errorContent);
    }
}

async function getSessionDetails(mainContent, path) {
    try {
        const sid = filterUriId(path);
        const result = await sessionDetailsRetrieval(sid);
        const pageContent = sessionDetailsPage(result.session);
        mainContent.replaceChildren(pageContent);
    } catch (error) {
        console.error(error);
        const errorContent = basicError(error.message);
        mainContent.replaceChildren(errorContent);
    }
}

/** Players */
async function getPlayer(mainContent, path) {
    try {
        const pid = filterUriId(path);
        const player = await playerDetailsRetrieval(pid);
        const pageContent = playerDetailsPage(player);
        mainContent.replaceChildren(pageContent);
    } catch (error) {
        console.error(error);
        const errorContent = basicError(error.message);
        mainContent.replaceChildren(errorContent);
    }
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