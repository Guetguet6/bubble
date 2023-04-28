package ca.uqac.bubble.profil

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import ca.uqac.bubble.MyDatabaseHelper
import ca.uqac.bubble.R

class ProfileActivity : ComponentActivity() {
    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var nameEditText: EditText
    private lateinit var nameTextView: TextView
    private lateinit var profileImage: ImageView

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profil)

        nameEditText = findViewById(R.id.nameEditText)
        nameTextView = findViewById(R.id.nameTextView)
        profileImage = findViewById(R.id.profileImageView)

        dbHelper = MyDatabaseHelper(this)

        // Récupération du nom d'utilisateur enregistré ou valeur par défaut
        val sharedPref = getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        val defaultName = getString(R.string.default_name)
        val userName = sharedPref.getString("USER_NAME", defaultName)

        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            val newName = nameEditText.text.toString()

            // Affichage du nom d'utilisateur


            // Mise à jour du nom d'utilisateur dans la base de données
            val db = dbHelper.writableDatabase
            val contentValues = ContentValues().apply {
                put("nom", newName)
            }
            val updatedRows = db.update("profil", contentValues, null, null)

            if (updatedRows > 0) {
                Toast.makeText(this, "Nom d'utilisateur modifié avec succès", Toast.LENGTH_SHORT).show()

                // Enregistrement du nouveau nom d'utilisateur dans les préférences partagées
                with(sharedPref.edit()) {
                    putString("USER_NAME", newName)
                    apply()
                }
            } else {
                Toast.makeText(this, "Erreur lors de la modification du nom d'utilisateur", Toast.LENGTH_SHORT).show()
            }
            nameTextView.setText(dbHelper.getNom())
        }

        // Charge l'image de profil depuis la base de données
        try{
            val imageBitmap = dbHelper.getImageBitmap()
            profileImage.setImageBitmap(imageBitmap)
        }
        catch (e :NullPointerException){
            e.printStackTrace()
        }


        profileImage.setOnClickListener {
            val options = arrayOf<CharSequence>("Prendre une photo", "Choisir depuis la galerie", "Annuler")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Ajouter une photo")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Prendre une photo" -> {
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        getImage.launch(takePicture)
                    }
                    options[item] == "Choisir depuis la galerie" -> {
                        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        getImage.launch(pickPhoto)
                    }
                    options[item] == "Annuler" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }

        nameTextView.setText(dbHelper.getNom())
    }

    private val getImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val imageUri = intent?.data
            val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            profileImage.setImageBitmap(imageBitmap)

            // Enregistre la nouvelle image dans la base de données
            dbHelper.updateImage(imageBitmap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            dbHelper.updateImage(imageBitmap)
            Toast.makeText(this, "Image modifiée avec succès", Toast.LENGTH_SHORT).show()
        }
    }
}
