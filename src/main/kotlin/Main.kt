import controllers.GameAPI
import models.Game
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.ScannerInput.readNextDouble
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}


private val gameAPI = GameAPI(JSONSerializer(File("games.json")))

fun main(args: Array<String>) {
    runMenu()
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addGame()
            2 -> listGames()
            3 -> updateGames()
            4 -> deleteGame()
            5 -> listGameGenre()
            6 -> GameCost()
            7 -> GameSuggestion()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> System.out.println("Invalid menu choice: ${option}")
        }
    } while (true)
}



fun mainMenu(): Int {
    return readNextInt(
        """ 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a Game                |
         > |   2) List Games                |
         > |   3) Update a Game             |
         > |   4) Delete a Game             |
         > ----------------------------------
         > |   5) Search by Genre           |
         > |   6) Calculate Cost            |
         > |   6) Random Suggestion         |
         > ----------------------------------
         > |   20) Save notes               |
         > |   21) Load notes               |
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">")
    )
}

fun addGame() {
    //logger.info { "addNote() function invoked" }
    val gameTitle = readNextLine("Enter the name of the game: ")
    val gameRating = readNextInt("Enter the rating you would give the game 1-10: ")
    val gameCost = readNextDouble("Enter a price: ")
    val gameGenre = readNextLine("Enter a genre: ")
    val developerName = readNextLine("Enter the developer name: ")
    var isGameOwned1 = readNextInt("Do you own this game? Type 1 for yes or 2 for no: ")
    if (isGameOwned1 == 1) {
        val isAdded = gameAPI.add(Game(gameTitle, gameRating, gameCost, gameGenre, developerName, isGameOwned = true))

        if (isAdded) {
            println("Added Successfully")
        } else {
            println("Add Failed")
        }
    } else {
        val isAdded = gameAPI.add(Game(gameTitle, gameRating, gameCost, gameGenre, developerName, isGameOwned = false))

        if (isAdded) {
            println("Added Successfully")
        } else {
            println("Add Failed")
        }
    }
}

fun listGames() {
    println(gameAPI.listAllGames())
}



fun updateGames() {
    listGames()
    if (gameAPI.numberOfGames() > 0) {
        //only ask the user to choose the game if game exists
        val indexToUpdate = readNextInt("Enter the index of the game to update: ")
        val any = if (gameAPI.isValidIndex(indexToUpdate)) {
            val gameTitle = readNextLine("Enter a title for the game: ")
            val gameCost = readNextDouble("Enter the price of the game: ")
            val gameGenre = readNextLine("Enter a genre for the game: ")

            //pass the index of the note and the new note details to GameAPI for updating and check for success.
            if (gameAPI.updateGame(gameTitle, gameRating, gameCost, gameGenre, developerName, isGameOwned = false)) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no games for this index number")
        }
    }
}

fun deleteGame() {
    //logger.info { "deleteNotes() function invoked" }
    listGames()
    if (gameAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun GameCost() {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        //only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        //pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.archiveNote(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun listGameGenre() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}


fun GameSuggestion() {
    TODO("Not yet implemented")
}

//------------------------------------
// PERSISTENCE METHODS
// ------------------------------------
fun save() {
    try {
        gameAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        gameAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp() {
    println("Exiting...bye")
    exit(0)
}