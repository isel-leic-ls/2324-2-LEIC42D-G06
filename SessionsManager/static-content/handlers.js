import { homePage, registerPage, loginPage } from "./pages/homePage.js"
import { gamesSearchPage, gamesListPage, gameDetailsPage } from "./pages/gamesPages.js"
import { sessionsSearchPage, sessionsListPage, sessionDetailsPage } from "./pages/sessionsPages.js"
import { playerDetailsPage, playersSearchPage, playersListPage } from "./pages/playerPages.js"
import { pagingButtons } from "./components/pagingButtons.js"
import { safeCall } from "./utils.js";
import { filterQueryParameters, filterResource } from "./uriparsers.js"
import { basicError } from "./components/basicError.js";
import { GameRepository } from "./data/gamesRequests.js";
import { PlayerRepository } from "./data/playerRequests.js";
import { SessionRepository } from "./data/sessionsRequests.js";
import { GameService } from "./services/gamesServices.js";
import { PlayerService } from "./services/playerServices.js";
import { SessionService } from "./services/sessionServices.js";
import { UserStorage } from "./storage.js";


// Initialize the UserStorage
const userStorage = new UserStorage();

// initiate repositories and services
const gameRepository = new GameRepository();
const playerRepository = new PlayerRepository();
const sessionRepository = new SessionRepository();

const gameService = new GameService(gameRepository);
const playerService = new PlayerService(playerRepository);
const sessionService = new SessionService(sessionRepository);

/** Home */
async function getHome(mainContent) {
    safeCall(mainContent, async () => {
        const { id, token } = userStorage.getUserInfo();

        const logoutFunction = (() => {
            userStorage.clearUserInfo();
            window.location.reload();
        });

        const pageContent = homePage(id, logoutFunction);
        mainContent.replaceChildren(pageContent);
    })
}

/** Games */
function getGamesSearch(mainContent) {
    const { id, token } = userStorage.getUserInfo();

    const createGameFunction = (async (name, dev, genres) => {
        try {
            const gid = await gameService.gameCreation(name, dev, genres, token);
            window.location.hash = "games/" + gid;
        } catch (error) {
            console.log(error)
            const errorContent = basicError(error.message, error.details)
            mainContent.replaceChildren(errorContent)
        }
    });

    const loggedIn = id != null;
    const pageContent = gamesSearchPage(createGameFunction, loggedIn); //boolean to check if the user is logged in
    mainContent.replaceChildren(pageContent);
}

async function getGamesList(mainContent, path) {
    safeCall(mainContent, async () => {
        const { skip, limit, genres, developer } = filterQueryParameters(path);
        const { games, total } = await gameService.gamesRetrieval(skip, limit, genres, developer);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = gamesListPage(games, buttons);
        mainContent.replaceChildren(pageContent);
    })
}

async function getPlayersList(mainContent, path) {
    safeCall(mainContent, async () => {
        const { skip, limit, name } = filterQueryParameters(path);
        console.log(skip, limit, name)
        const { players, total } = await playerService.playersRetrieval(name, skip, limit);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = playersListPage(players, buttons);
        mainContent.replaceChildren(pageContent);
    })
}

async function getGameDetails(mainContent, path) {
    safeCall(mainContent, async () => {
        const { id, token } = userStorage.getUserInfo();
        const gid = filterResource(path);
        const game = await gameService.gameDetailsRetrieval(gid);

        const createSessionFunction = (async (gid, capacity, date) => {
            try {
                const sid = await sessionService.sessionCreation(gid, capacity, date, token);
                window.location.hash = "sessions/" + sid;
            } catch (error) {
                const errorContent = basicError(error.message, error.details)
                mainContent.replaceChildren(errorContent)
            }
        });

        const loggedIn = id != null; //boolean to check if the user is logged in
        const pageContent = gameDetailsPage(game, createSessionFunction, loggedIn);
        mainContent.replaceChildren(pageContent);
    })
}

/** Sessions */
function getSessionsSearch(mainContent) {
    const pageContent = sessionsSearchPage();
    mainContent.replaceChildren(pageContent);
}

function getPlayersSearch(mainContent) {
    const pageContent = playersSearchPage();
    mainContent.replaceChildren(pageContent);
}

async function getSessionsList(mainContent, path) {
    safeCall(mainContent, async () => {
        const { gid, date, state, pid, skip, limit } = filterQueryParameters(path);
        const { sessions, total } = await sessionService.sessionsRetrieval(gid, date, state, pid, skip, limit);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = sessionsListPage(sessions, buttons);
        mainContent.replaceChildren(pageContent);
    })
}

async function getSessionDetails(mainContent, path) {
    safeCall(mainContent, async () => {
        const { id, token } = userStorage.getUserInfo();
        const sid = filterResource(path);
        const result = await sessionService.sessionDetailsRetrieval(sid);

        const leaveSessionFunction = (async (id) => {
            await sessionService.sessionLeave(id, token);
            window.location.reload();
        });

        const updateSessionFunction = (async (id, capacity, date) => {
            try {
                await sessionService.sessionUpdate(id, capacity, date, token);
                window.location.reload();
            } catch (error) {
                const errorContent = basicError(error.message, error.details)
                mainContent.replaceChildren(errorContent)
            }
        });

        const deleteSessionFunction = (async (id) => {
            await sessionService.sessionLeave(id, token);
            window.location.reload();
        });

        const joinSessionFunction = (async (id) => {
            await sessionService.sessionJoin(id, token);
            window.location.reload();
        });

        const pageContent = sessionDetailsPage(
            result.session,
            leaveSessionFunction,
            updateSessionFunction,
            deleteSessionFunction,
            joinSessionFunction,
            id
        );
        mainContent.replaceChildren(pageContent);
    })
}

/** Players */
async function getPlayer(mainContent, path) {
    safeCall(mainContent, async () => {
        const pid = filterResource(path);
        const player = await playerService.playerDetailsRetrieval(pid);
        const pageContent = playerDetailsPage(player);
        mainContent.replaceChildren(pageContent);
    })
}

async function getGamesSearchByName(mainContent, path) {
    safeCall(mainContent, async () => {
        const { gname, skip, limit } = filterQueryParameters(path);
        const { games, total } = await gameService.gamesByNameRetrieval(gname, skip, limit);
        const buttons = pagingButtons(parseInt(skip), parseInt(limit), total, path)
        const pageContent = gamesListPage(games, buttons);
        mainContent.replaceChildren(pageContent);
    })
}

async function getRegister(mainContent) {
    safeCall(mainContent, async () => {
        const registerFunction = (async (name, email, password) => {
            try {
                await playerService.playerRegistration(name, email, password);
                window.location.hash = "login";
            } catch (error) {
                const errorContent = basicError(error.message, error.details)
                mainContent.replaceChildren(errorContent)
            }
        });
        const pageContent = registerPage(registerFunction);
        mainContent.replaceChildren(pageContent);
    })
}

async function getLogin(mainContent) {
    safeCall(mainContent, async () => {
        const loginFunction = (async (username, password) => {
            try {
                const response = await playerService.playerLogin(username, password);
                userStorage.setUserInfo(response.id, response.token); // set the user info in the storage
                window.location.hash = "home";
            } catch (error) {
                const errorContent = basicError(error.message, error.details)
                mainContent.replaceChildren(errorContent)
            }
        });
        const pageContent = loginPage(loginFunction);
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
    getPlayersList,
    getPlayersSearch,
    getGamesSearchByName,
    getRegister,
    getLogin
}

export default handlers