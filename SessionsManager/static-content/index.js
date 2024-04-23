import router from "./router.js";
import handlers from "./handlers.js";


window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler() { //called when the page is loaded
    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("games", handlers.getGamesSearch)
    router.addRouteHandler("games/list", handlers.getGamesList)
    router.addRouteHandler("sessions", handlers.getSessionsSearch)
    router.addRouteHandler("sessions/list", handlers.getSessionsList)
    router.addRouteHandler("sessions/:sid", handlers.getSessionDetails)
    router.addRouteHandler("players/:pid", handlers.getPlayer)
    router.addRouteHandler("players/email/:email", handlers.getPlayerByEmail)
    router.addRouteHandler("games/:gid", handlers.getGameDetails)
    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")
    hashChangeHandler()
}

export function hashChangeHandler() { //called when the hash changes
    const mainContent = document.getElementById("mainContent")
    const path = window.location.hash.replace("#", "")

    const handler = router.getRouteHandler(path)
    handler(mainContent, path)
}