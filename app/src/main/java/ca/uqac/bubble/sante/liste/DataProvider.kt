package ca.uqac.bubble.sante.liste

import ca.uqac.bubble.R

object DataProvider {
    val liste = listOf(
        Categorie(
            id = 1,
            title = "Cohérence Cardiaque",
            description = "Vivamus iaculis egestas leo, tincidunt sollicitudin nibh. Cras imperdiet lorem eget lacus lacinia interdum. Phasellus finibus consectetur tempus. In imperdiet. ",
            imgagePath = R.drawable.plage,
            classPath = CoherenceCardiaqueActivity::class.java,
        ),
        Categorie(
            id = 2,
            title = "Lorem ipsum dolor sit amet",
            description = "Vivamus iaculis egestas leo, tincidunt sollicitudin nibh. Cras imperdiet lorem eget lacus lacinia interdum. Phasellus finibus consectetur tempus. In imperdiet. "
        ),
        Categorie(
            id = 3,
            title = "Lorem ipsum dolor sit amet",
            description = "Vivamus iaculis egestas leo, tincidunt sollicitudin nibh. Cras imperdiet lorem eget lacus lacinia interdum. Phasellus finibus consectetur tempus. In imperdiet. "
        ),
        Categorie(
            id = 4,
            title = "Lorem ipsum dolor sit amet",
            description = "Vivamus iaculis egestas leo, tincidunt sollicitudin nibh. Cras imperdiet lorem eget lacus lacinia interdum. Phasellus finibus consectetur tempus. In imperdiet. "
        )
    )
}