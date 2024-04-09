import router from "../router.js";
import handlers from "../handlers.js";

  describe('router', function () {

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
})
