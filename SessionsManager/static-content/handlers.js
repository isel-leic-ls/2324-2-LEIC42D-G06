import {homePage} from "./pages/homePage.js"
import {gamesSearchPage, gamesListPage, gameDetailsPage} from "./pages/gamesPages.js"
import {sessionsSearchPage, sessionsListPage, sessionDetailsPage} from "./pages/sessionsPages.js"
import {playerDetailsPage} from "./pages/playerPages.js"
import {pagingButtons} from "./components/pagingButtons.js"
import {safeCall} from "./utils.js";
import {filterQueryParameters, filterResource} from "./uriparsers.js"
import {sessionsRetrieval, sessionDetailsRetrieval, sessionCreation} from "./services/sessionServices.js"
import {gamesRetrieval, gameDetailsRetrieval, gamesByNameRetrieval } from "./services/gamesServices.js"
import {playerDetailsRetrieval, playerIdRetrieval} from "./services/playerServices.js"
import {div, button} from "./tags.js";
import {closeModal} from "./components/modal.js";

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
        const gid = filterResource(path);
        const game = await gameDetailsRetrieval(gid);
        const createSessionFunction = (async (gid, capacity, date) => {
            const sid = await sessionCreation(gid, capacity, date);
            console.log("Session created with id: " + sid);
            closeModal();
            window.location.hash = "sessions/" + sid;
        });
        const pageContent = gameDetailsPage(game, createSessionFunction);
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
        const sid = filterResource(path);
        const result = await sessionDetailsRetrieval(sid);
        const pageContent = sessionDetailsPage(result.session);
        mainContent.replaceChildren(pageContent);
    })
}

/** Players */
async function getPlayer(mainContent, path) {
    safeCall(mainContent,async ()=> {
        const pid = filterResource(path);
        const player = await playerDetailsRetrieval(pid);
        const pageContent = playerDetailsPage(player);
        mainContent.replaceChildren(pageContent);
    })
}

async function getGamesSearchByName(mainContent, path) {
    safeCall(mainContent,async ()=> {
        const name = filterResource(path);
        const game = await gamesByNameRetrieval(name); // This is only one game.. backend needs to retrieve a list of games
        const element = div(                           // This should also support pagination
            {},
            "Games",
            div({}, game.name),
            div({}, "Developer: " + game.dev),
            div({}, "Genres: " + game.genres.join(", ")),
            button( {onClick : () => {} }, "Create a session with this game")
        )
        mainContent.replaceChildren(element);
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
    getPlayer,
    getGamesSearchByName
}

export default handlers