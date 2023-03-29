package ca.uqac.bubble.profil

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ca.uqac.bubble.R

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_IMAGE = 1001
    }

    private lateinit var profileImage: ImageView
    private lateinit var changePhotoButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profil)

        profileImage = findViewById(R.id.profile_image)
        changePhotoButton = findViewById(R.id.change_photo_button)
        nameEditText = findViewById(R.id.name_edit_text)

        // Get the default user name from the string resources
        userName = getString(R.string.default_name)

        nameEditText.setOnEditorActionListener { _, _, _ ->
            // Update the user name with the new name entered by the user
            userName = nameEditText.text.toString()
            true
        }

        changePhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            // Update the profile image with the selected image
            val imageUri = data?.data
            profileImage.setImageURI(imageUri)
        }
    }
}