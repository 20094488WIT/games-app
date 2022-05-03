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

    fun updateGame(
        indexToUpdate: String,
        game: Int,
        gameCost: Double,
        gameGenre: String,
        developerName: String,
        isGameOwned: Boolean

    ): Boolean {
        //find the Game object by the index number
        val foundGame = findGame(indexToUpdate)

        //if the Game exists, use the Game details passed as parameters to update the found Game in the ArrayList.
        if ((foundGame != null) && (game != null)) {
            foundGame.gameTitle = game.gameTitle
            foundGame.gameCost = game.gameCost
            foundGame.gameGenre = game.gameGenre
            foundGame.developerName = game.developerName

          return true
        }

        //if the Game was not found, return false, indicating that the update was not successful
        return false


    }




    //----------------------------------------------
    //  LISTING METHODS
    //----------------------------------------------

    fun listAllGames(): String =
        if  (games.isEmpty()) "No Games stored"
        else formatListString(games)


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
                "No Games with genre: $Genre"
            }
        }
    }

    //----------------------------------------------
    //  COUNTING METHODS
    //----------------------------------------------
    fun numberOfGames(): Int {
        return games.size
    }

    fun numberOfOwnedGames(): Int = games.count { game: Game -> game.isGameOwned }


    //----------------------------------------------
    //  SEARCHING METHODS
    //----------------------------------------------
     fun findGame(index: String): Game? {
        return if (isValidListIndex(index.toInt(), games)) {
            games[index.toInt()]
        } else null
    }

    fun isValidIndex(index: Int) :Boolean {
        return isValidListIndex(index, games);
    }

    fun searchByGenre (searchString : String) =
        formatListString(
            games.filter { Game -> Game.gameGenre.contains(searchString, ignoreCase = true) })

    //----------------------------------------------
    //  HELPER METHODS
    //----------------------------------------------
    private fun formatListString(GamesToFormat : List<Game>) :String =
        GamesToFormat
            .joinToString (separator = "\n") { Game ->
                games.indexOf(Game).toString() + ": " + Game.toString() }

    //----------------------------------------------
    //  PERSISTENCE METHODS
    //----------------------------------------------
    @Throws(Exception::class)
    fun load() {
        games = serializer.read() as ArrayList<Game>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(games)
    }

}


