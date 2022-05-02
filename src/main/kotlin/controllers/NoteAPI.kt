package controllers

import models.Game
import persistence.Serializer
import utils.Utilities.isValidListIndex

class GameAPI(serializerType: Serializer){

     private var serializer: Serializer = serializerType
     private var games = ArrayList<Game>()

    //----------------------------------------------
    //  CRUD METHODS
    //----------------------------------------------
    fun add(game: Game): Boolean {
        return games.add(game)
    }

    fun deleteGame(indexToDelete: Int): Game? {
        return if (isValidListIndex(indexToDelete, games)) {
            games.removeAt(indexToDelete)
        } else null
    }

    fun updateGame(indexToUpdate: Int, game: Game?): Boolean {
        //find the Game object by the index number
        val foundGame = findGame(indexToUpdate)

        //if the Game exists, use the Game details passed as parameters to update the found Game in the ArrayList.
        if ((foundGame != null) && (game != null)) {
            foundGame.GameTitle = game.GameTitle
            foundGame.GamePriority = game.GamePriority
            foundGame.GameCategory = game.GameCategory
          return true
        }

        //if the Game was not found, return false, indicating that the update was not successful
        return false
    }

    fun archiveGame(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val gameToArchive = games[indexToArchive]
            if (!gameToArchive.isGameArchived) {
                gameToArchive.isGameArchived = true
                return true
            }
        }
        return false
    }

    //----------------------------------------------
    //  LISTING METHODS
    //----------------------------------------------

    fun listAllGames(): String =
        if  (games.isEmpty()) "No Games stored"
        else formatListString(games)

    fun listActiveGames(): String =
        if  (numberOfActiveGames() == 0)  "No active Games stored"
        else formatListString(games.filter { Game -> !Game.isGameArchived})

    fun listArchivedGames(): String =
        if  (numberOfArchivedGames() == 0) "No archived Games stored"
        else formatListString(games.filter { Game -> Game.isGameArchived})

    fun listGamesBySelectedGenre(Genre: String): String {
        return if (games.isEmpty()) {
            "No Games stored"
        } else {
            var listOfGames = ""
            for (i in games.indices) {
                if (games[i].gameGenre == Genre) {
                    listOfGames +=
                        """$i: ${games[i]}
                        """.trimIndent()
                }
            }
            if (listOfGames.equals("")) {
                "No Games with genre: $Genre"
            } else {
                "${numberOfGamesByPriority(priority)} Games with priority $priority: $listOfGames"
            }
        }
    }

    //----------------------------------------------
    //  COUNTING METHODS
    //----------------------------------------------
    fun numberOfGames(): Int {
        return Games.size
    }

    fun numberOfArchivedGames(): Int = Games.count { game: Game -> game.isGameArchived }
    fun numberOfActiveGames(): Int   = Games.count { game: Game -> !game.isGameArchived }
    fun numberOfGamesByPriority(priority: Int): Int = Games.count { game: Game -> game.GamePriority == priority }

    //----------------------------------------------
    //  SEARCHING METHODS
    //----------------------------------------------
     fun findGame(index: Int): Game? {
        return if (isValidListIndex(index, Games)) {
            Games[index]
        } else null
    }

    fun isValidIndex(index: Int) :Boolean {
        return isValidListIndex(index, Games);
    }

    fun searchByTitle (searchString : String) =
        formatListString(
            Games.filter { Game -> Game.GameTitle.contains(searchString, ignoreCase = true) })

    //----------------------------------------------
    //  HELPER METHODS
    //----------------------------------------------
    private fun formatListString(GamesToFormat : List<Game>) :String =
        GamesToFormat
            .joinToString (separator = "\n") { Game ->
                Games.indexOf(Game).toString() + ": " + Game.toString() }

    //----------------------------------------------
    //  PERSISTENCE METHODS
    //----------------------------------------------
    @Throws(Exception::class)
    fun load() {
        Games = serializer.read() as ArrayList<Game>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(Games)
    }

}

