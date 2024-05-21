import router from "../router.js";
import handlers from "../handlers.js";
import {hashChangeHandler} from "../index.js";

describe('router', function () {
    const timeout = 300;
    window.addEventListener('hashchange', hashChangeHandler);

    it('should find home', function () {

        router.addRouteHandler("home", handlers.getHome)
        const handler = router.getRouteHandler("home")
        handler.name.should.be.equal("getHome")
    })

    it('undefined route default to notfoundroutehandler', function () {
        const handler = router.getRouteHandler("HOMEHOMEHOMEHOME")
        handler.name.should.be.equal("notFoundRouteHandler")
    })

    it('should find getgamessearch ', function () {

        router.addRouteHandler("games", handlers.getGamesSearch)
        const handler = router.getRouteHandler("games")
        handler.name.should.be.equal("getGamesSearch")
    })

    it('should find get games list', function () {
        router.addRouteHandler("games/list", handlers.getGamesList)
        const handler = router.getRouteHandler("games/list")
        handler.name.should.be.equal("getGamesList")
    })

    it('should find get sessionsSearch', function () {
        router.addRouteHandler("sessions", handlers.getSessionsSearch)
        const handler = router.getRouteHandler("sessions")
        handler.name.should.be.equal("getSessionsSearch")
    })

    it('should find get game details', function () {
        router.addRouteHandler("games/:gid", handlers.getGameDetails)
        const handler = router.getRouteHandler("games/100")
        handler.name.should.be.equal("getGameDetails")
    })

    it('should find get sessions list', function () {
        router.addRouteHandler("sessions/list", handlers.getSessionsList)
        const handler = router.getRouteHandler("sessions/list")
        handler.name.should.be.equal("getSessionsList")
    })

    it('should find get session details', function () {
        router.addRouteHandler("sessions/:sid", handlers.getSessionDetails)
        const handler = router.getRouteHandler("sessions/:sid")
        handler.name.should.be.equal("getSessionDetails")
    })

    it('should find get player ', function () {
        router.addRouteHandler("player/:pid", handlers.getPlayer)
        const handler = router.getRouteHandler("player/:pid")
        handler.name.should.be.equal("getPlayer")
    })

    it('should find get sessions list (with query string) ', function () {
        router.addRouteHandler("sessions/list", handlers.getSessionsList)
        const handler = router.getRouteHandler("sessions/list?gid=100")
        handler.name.should.be.equal("getSessionsList")
    })

    it('should find get games list (with query string) ', function () {
        router.addRouteHandler("games/list", handlers.getGamesList)
        const handler = router.getRouteHandler("games/list?genres=action&developer=valve")
        handler.name.should.be.equal("getGamesList")
    })

    it('should show message "Failed to retrieve player details"', function (done) {
        router.addRouteHandler("players/:pid", handlers.getPlayer);
        window.location.hash = "players/9999999";

        setTimeout(() => {
            const sut = document.getElementById("basicError");
            sut.innerText.should.be.equal("Failed to retrieve player details\n\nDetails: Player 9999999 does not exist\n\nReturn Home")
            document.getElementById("mainContent").removeChild(sut)
            done();
        }, timeout);
    });

    it('should show message "Invalid player ID"', function (done) {
        router.addRouteHandler("players/:pid", handlers.getPlayer);
        window.location.hash = "players/1";

        setTimeout(() => {
            const sut = document.getElementById("basicError");
            sut.innerText.should.be.equal("Invalid player ID\n\nReturn Home")
            document.getElementById("mainContent").removeChild(sut)
            done();
        }, timeout);
    });

    it('should show message "Failed to retrieve game details"', function (done) {
        router.addRouteHandler("games/:gid", handlers.getGameDetails);
        window.location.hash = "games/9999999";

        setTimeout(() => {
            const sut = document.getElementById("basicError");
            sut.innerText.should.be.equal("Failed to retrieve game details\n\nDetails: Game 9999999 does not exist\n\nReturn Home")
            document.getElementById("mainContent").removeChild(sut)
            done();
        }, timeout);
    });

    it('should show message "Invalid game ID"', function (done) {
        router.addRouteHandler("games/:gid", handlers.getGameDetails);
        window.location.hash = "games/1";

        setTimeout(() => {
            const sut = document.getElementById("basicError");
            sut.innerText.should.be.equal("Invalid game ID\n\nReturn Home")
            document.getElementById("mainContent").removeChild(sut)
            done();
        }, timeout);
    })

    it('should show message "Failed to retrieve session details', function (done) {
        router.addRouteHandler("sessions/:sid", handlers.getSessionDetails);
        window.location.hash = "sessions/9999999";

        setTimeout(() => {
            const sut = document.getElementById("basicError");
            sut.innerText.should.be.equal("Failed to retrieve session details\n\nDetails: Session 9999999 does not exist\n\nReturn Home")
            document.getElementById("mainContent").removeChild(sut)
            done();
        }, timeout);
    })

    it('should show message "Invalid session ID"', function (done) {
        router.addRouteHandler("sessions/:sid", handlers.getSessionDetails);
        window.location.hash = "sessions/1";

        setTimeout(() => {
            const sut = document.getElementById("basicError");
            sut.innerText.should.be.equal("Invalid session ID\n\nReturn Home")
            document.getElementById("mainContent").removeChild(sut)
            done();
        }, timeout);
    })

    /*
        it('should find player with id 1000', function (done) {
            router.addRouteHandler("players/:pid", handlers.getPlayer);
            window.location.hash = "players/1000";

            setTimeout(() => {
                const sut = document.getElementById("playerDetails");
                console.log(sut)
                sut.innerText.should.be.equal(
                    "Player Details\nName: Pedro\nE-mail: pedro@hotmail.com\nSearch sessions with this player\nReturn Home"
                )
                document.getElementById("mainContent").removeChild(sut);
                done();
            }, timeout);
        })


        it('should find game with id 100', function (done) {
            router.addRouteHandler("games/:gid", handlers.getGameDetails);
            window.location.hash = "games/100";

            setTimeout(() => {
                const sut = document.getElementById("gameDetails");
                sut.innerText.should.be.equal(
                    "Game Details\nName: Lord of the Rings Online\nDeveloper: Dev123\nGenres: RPG, Adventure\n" +
                    "Search sessions with this game\nReturn Home"
                )
                document.getElementById("mainContent").removeChild(sut);
                done();
            }, timeout);
        })
    */

})
