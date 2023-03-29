package ca.uqac.bubble.profil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import ca.uqac.bubble.MainActivity
import ca.uqac.bubble.R
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException

class ProfileActivity : ComponentActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var sharedPref: SharedPreferences
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profil)

        // Initialisation des vues
        nameEditText = findViewById(R.id.nameEditText)
        profileImageView = findViewById(R.id.profileImageView)
        nameTextView = findViewById(R.id.nameTextView)

        // Initialisation des préférences partagées
        sharedPref = getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)

        // Récupération du nom d'utilisateur enregistré ou valeur par défaut
        val userName = sharedPref.getString("USER_NAME", getString(R.string.default_name))

        // Affichage du nom d'utilisateur
        nameTextView.text = userName

        // Récupération de l'image de profil enregistrée ou valeur par défaut
        val profileImageString = sharedPref.getString("PROFILE_IMAGE", "")
        if (!profileImageString.isNullOrEmpty()) {
            profileImageView.setImageBitmap(decodeStringToImage(profileImageString))
        }

        // Gestionnaire de clics du bouton Valider
        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            // Récupération du nouveau nom d'utilisateur
            val newUserName = nameEditText.text.toString()

            // Enregistrement du nouveau nom d'utilisateur dans les préférences partagées
            with(sharedPref.edit()) {
                putString("USER_NAME", newUserName)
                apply()
            }

            // Enregistrement de la nouvelle image de profil dans les préférences partagées
            if (selectedImageUri != null) {
                val bitmap = getBitmapFromUri(selectedImageUri!!)
                if (bitmap != null) {
                    val profileImageString = encodeImageToString(bitmap)
                    // Faire quelque chose avec profileImageString...
                } else {
                    // Gérer le cas où la valeur retournée par getBitmapFromUri est nulle...
                }
                with(sharedPref.edit()) {
                    putString("PROFILE_IMAGE", profileImageString)
                    apply()
                }
            }

            // Démarrage d'une nouvelle activité
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Gestionnaire de clics de l'image de profil
        profileImageView.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 1000)
        }
    }

    // Convertit une image Bitmap en une chaîne encodée en base64
    private fun encodeImageToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Récupération de l'image sélectionnée par l'utilisateur
            val imageUri: Uri = data.data!!
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Affichage de l'image dans l'ImageView
            profileImageView.setImageBitmap(bitmap)

            // Conversion de l'image en une chaîne de caractères encodée en Base64
            val imageString = encodeImageToString(bitmap)

            // Enregistrement de la chaîne encodée dans les préférences partagées
            with(sharedPref.edit()) {
                putString("USER_IMAGE", imageString)
                apply()
            }
        }
    }

    fun decodeStringToImage(imageString: String): Bitmap {
        val decodedBytes = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}