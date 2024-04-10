import {homePage} from "./pages/homePage.js"
import {gamesSearchPage, gamesListPage, gameDetailsPage} from "./pages/gamesPages.js"
import {sessionsSearchPage, sessionsListPage, sessionDetailsPage} from "./pages/sessionsPages.js"
import {playerDetailsPage} from "./pages/playerPages.js"
import {pagingButtons} from "./components/pagingButtons.js"
import {safeCall} from "./utils.js";
import {filterQueryParameters, filterUriId} from "./uriparsers.js"
import {sessionsRetrieval, sessionDetailsRetrieval} from "./services/sessionServices.js"
import {gamesRetrieval, gameDetailsRetrieval} from "./services/gamesServices.js"
import {playerDetailsRetrieval, playerIdRetrieval} from "./services/playerServices.js"





/** Home */
async function getHome(mainContent) {
    safeCall(mainContent, async ()=> {
        const player = await playerIdRetrieval()
        const pageContent = homePage(player);
        mainContent.replaceChildren(pageContent);
    })
}

/** Games */
function getGamesSearch(mainContent) {
    const pageContent = gamesSearchPage();
    mainContent.replaceChildren(pageContent);
}

async function getGamesList(mainContent, path) {
    safeCall(mainContent, async ()=> {
        const {skip, limit, genres, developer} = filterQueryParameters(path);
        const {games, total} = await gamesRetrieval(skip, limit, genres, developer);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = gamesListPage(games, buttons);
        mainContent.replaceChildren(pageContent);
    })
}

async function getGameDetails(mainContent, path) {
    safeCall(mainContent,async ()=> {
        const gid = filterUriId(path);
        const game = await gameDetailsRetrieval(gid);
        const pageContent = gameDetailsPage(game);
        mainContent.replaceChildren(pageContent);
    })

}

/** Sessions */
function getSessionsSearch(mainContent) {
    const pageContent = sessionsSearchPage();
    mainContent.replaceChildren(pageContent);
}

async function getSessionsList(mainContent, path) {
    safeCall(mainContent,async ()=> {
        const {gid, date, state, pid, skip, limit} = filterQueryParameters(path);
        const {sessions, total} = await sessionsRetrieval(gid, date, state, pid, skip, limit);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = sessionsListPage(sessions, buttons);
        mainContent.replaceChildren(pageContent);
    })
}

async function getSessionDetails(mainContent, path) {
    safeCall(mainContent,async ()=>{
        const sid = filterUriId(path);
        const result = await sessionDetailsRetrieval(sid);
        const pageContent = sessionDetailsPage(result.session);
        mainContent.replaceChildren(pageContent);
    })
}

/** Players */
async function getPlayer(mainContent, path) {
    safeCall(mainContent,async ()=> {
        const pid = filterUriId(path);
        const player = await playerDetailsRetrieval(pid);
        const pageContent = playerDetailsPage(player);
        mainContent.replaceChildren(pageContent);
    })
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