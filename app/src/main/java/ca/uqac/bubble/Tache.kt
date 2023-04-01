package ca.uqac.bubble

import java.time.LocalDate
import java.util.*

data class Tache(val id: Int = ID_TACHES, var titre: String, var categorie: String, var faite: Boolean = false, var deadline: LocalDate = LocalDate.now(), var urgence: Int = 0) {
    companion object{
        var ID_TACHES = 0
    }
    init {
        ID_TACHES++
    }
}