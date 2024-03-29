import router from "./router.js";
import handlers from "./handlers.js";


window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler() { //called when the page is loaded
    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("games", handlers.getGamesSearch)
    router.addRouteHandler("games/list", handlers.getGamesList)
    router.addRouteHandler("sessions", handlers.getSessions)
    router.addRouteHandler("players/:pid", handlers.getPlayer) //HARD-CODED ID
    router.addRouteHandler("games/:gid", handlers.getGame)
    //router.addRouteHandler("/players", handlers.getPlayerByToken)
    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")
    hashChangeHandler()
}

function hashChangeHandler() { //called when the hash changes
    const mainContent = document.getElementById("mainContent")
    const path = window.location.hash.replace("#", "")

    const handler = router.getRouteHandler(path)
    handler(mainContent, path)
}