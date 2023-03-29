package ca.uqac.bubble

import java.time.LocalDate
import java.util.*

data class Tache(
    val titre: String,
    val categorie: String,
    var faite: Boolean = false,
    val deadline: LocalDate = LocalDate.now(),
    val urgence: Int = 0
)