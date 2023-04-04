package ca.uqac.bubble.sante.liste

import ca.uqac.bubble.R

data class Categorie(
    val id: Int,
    val title: String,
    val description: String,
    val imgagePath: Int = R.drawable.no_image,
    val classPath: Class<*>? = null,
)
