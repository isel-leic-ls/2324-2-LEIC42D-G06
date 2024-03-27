package pt.isel.ls.services

import SessionRepo
import pt.isel.ls.repo.interfaces.GamesRepo
import pt.isel.ls.repo.interfaces.PlayersRepo


class Services(
    gamesRepo: GamesRepo,
    playersRepo: PlayersRepo,
    sessionRepo: SessionRepo
) {
    val gameService = GamesServices(gamesRepo, playersRepo)
    val playerService = PlayerServices(playersRepo)
    val sessionService = SessionServices(playersRepo, gamesRepo, sessionRepo)
}