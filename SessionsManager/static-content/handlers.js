import {homePage} from "./pages/homePage.js"
import {gamesSearchPage, gamesListPage, gameDetailsPage} from "./pages/gamesPages.js"
import {sessionsSearchPage, sessionsListPage, sessionDetailsPage} from "./pages/sessionsPages.js"
import {playerDetailsPage} from "./pages/playerPages.js"
import {pagingButtons} from "./components/pagingButtons.js"
import {safeCall} from "./utils.js";
import {filterQueryParameters, filterResource} from "./uriparsers.js"
import {
    sessionsRetrieval,
    sessionDetailsRetrieval,
    sessionCreation,
    sessionLeave,
    sessionUpdate,
    sessionJoin
} from "./services/sessionServices.js"
import {gamesRetrieval, gameDetailsRetrieval, gamesByNameRetrieval, gameCreation} from "./services/gamesServices.js"
import {playerDetailsRetrieval, playerIdRetrieval} from "./services/playerServices.js"
import {basicError} from "./components/basicError.js";

/** Home */
async function getHome(mainContent) {
    safeCall(mainContent, async () => {
        const player = await playerIdRetrieval()
        const pageContent = homePage(player);
        mainContent.replaceChildren(pageContent);
    })
}

/** Games */
function getGamesSearch(mainContent) {
    const createGameFunction = (async (name, dev, genres) => {
        try{
            const gid = await gameCreation(name, dev, genres);
            window.location.hash = "games/" + gid;
        }catch(error){
            const errorContent = basicError(error.message)
            mainContent.replaceChildren(errorContent)
        }

    });
    const pageContent = gamesSearchPage(createGameFunction);
    mainContent.replaceChildren(pageContent);
}

async function getGamesList(mainContent, path) {
    safeCall(mainContent, async () => {
        const {skip, limit, genres, developer} = filterQueryParameters(path);
        const {games, total} = await gamesRetrieval(skip, limit, genres, developer);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = gamesListPage(games, buttons);
        mainContent.replaceChildren(pageContent);
    })
}

async function getGameDetails(mainContent, path) {
    safeCall(mainContent, async () => {
        const gid = filterResource(path);
        const game = await gameDetailsRetrieval(gid);
        const createSessionFunction = (async (gid, capacity, date) => {
            try {
                const sid = await sessionCreation(gid, capacity, date);
                window.location.hash = "sessions/" + sid;
            } catch(error) {
                const errorContent = basicError(error.message)
                mainContent.replaceChildren(errorContent)
            }
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
    safeCall(mainContent, async () => {
        const {gid, date, state, pid, skip, limit} = filterQueryParameters(path);
        const {sessions, total} = await sessionsRetrieval(gid, date, state, pid, skip, limit);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = sessionsListPage(sessions, buttons);
        mainContent.replaceChildren(pageContent);
    })
}

function isInSession(session, pid) {
    return session.players.includes(pid);
}

function isOwner(session, pid) {
    return session.players[0] == pid;
}

async function getSessionDetails(mainContent, path) {
    safeCall(mainContent, async () => {
        const sid = filterResource(path);
        const result = await sessionDetailsRetrieval(sid);
        const leaveSessionFunction = (async (id) => {
            await sessionLeave(id);
            window.location.reload();
        });

        const updateSessionFunction = (async (id, capacity, date) => {
            await sessionUpdate(id, capacity, date);
            window.location.reload();
        });

        const deleteSessionFunction = (async (id) => {
            await sessionLeave(id);
            window.location.reload();
        });

        const joinSessionFunction = (async (id) => {
            await sessionJoin(id);
            window.location.reload();
        });

        const pageContent = sessionDetailsPage(result.session, isInSession, isOwner,
            leaveSessionFunction, updateSessionFunction, deleteSessionFunction, joinSessionFunction);
        mainContent.replaceChildren(pageContent);
    })
}

/** Players */
async function getPlayer(mainContent, path) {
    safeCall(mainContent, async () => {
        const pid = filterResource(path);
        const player = await playerDetailsRetrieval(pid);
        const pageContent = playerDetailsPage(player);
        mainContent.replaceChildren(pageContent);
    })
}

async function getGamesSearchByName(mainContent, path) {
    safeCall(mainContent, async () => {
        const {gname, skip, limit} = filterQueryParameters(path);
        const {games, total} = await gamesByNameRetrieval(gname, skip, limit);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = gamesListPage(games, buttons);
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
    getPlayer,
    getGamesSearchByName
}

export default handlers