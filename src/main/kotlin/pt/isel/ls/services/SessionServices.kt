package pt.isel.ls.services

import pt.isel.ls.repo.SessionRepo
import java.util.*

class SessionServices(private val repo : SessionRepo)  {

    fun createSession(token : String, gid : Int, capacity : Int) : Int {
        TODO()
        // Get user by token (token is valid)
        // Validate the game exists
        // Validate the capacity
        // Call the repository function
    }

    fun addPlayerToSession(token : String, sid : Int, pid : Int) {
        TODO()
        // Get user by token (token is valid)
        // Validate if the creator of the Session is making this request
        // Validate Session
        // Validate Player
        // Call the repository function
    }

    fun getSession(sid : Int) {
        TODO()
        // Call the repository function
    }

    fun getListOfSessions(gid : Int, startDate : Date?, state : String?, pid : Int?, skip : Int, limit : Int) {
        TODO()
        // Validate game
        // Validate state
        // Validate player
        // Call the repository function
    }

}