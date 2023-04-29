package ca.uqac.bubble.sante.liste

import ca.uqac.bubble.R

data class Categorie(
    val id: Int = Identifiant,
    val title: String = "Lorem ipsum dolor sit amet",
    val description: String = "Vivamus iaculis egestas leo, tincidunt sollicitudin nibh. Cras imperdiet lorem eget lacus lacinia interdum. Phasellus finibus consectetur tempus. In imperdiet. ",
    val imgagePath: Int = R.drawable.no_image,
    val classPath: Class<*>? = null,
) {
    companion object{
        var Identifiant = 0
    }
    init {
        Identifiant++
    }
}
