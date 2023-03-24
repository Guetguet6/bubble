package ca.uqac.bubble

import java.time.LocalDate
import java.util.*

data class Tache(
    val titre: String,
    val categorie: String,
    val faite: Boolean = false,
    val deadline: LocalDate,
    val urgence: Int = 0
)