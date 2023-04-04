package ca.uqac.bubble

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "bubble.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE profil (id INTEGER PRIMARY KEY, nom TEXT, image BLOB)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS profil"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)

        val contentValues = ContentValues().apply {
            put("nom", "Utilisateur")
        }

        db?.insert("profil", null, contentValues)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun updateNom(nouveauNom: String) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("nom", nouveauNom)
        }

        db.update("profil", contentValues, "id = 1", null)
    }

    fun getNom(): String {
        val db = readableDatabase
        var nom = "Utilisateur"
        val cursor = db.query("profil", arrayOf("nom"), "id = 1", null, null, null, null)
        cursor.moveToFirst()
        val nomIndex = cursor.getColumnIndex("nom")
        if (nomIndex >= 0) {
            nom = cursor.getString(nomIndex)
        }
        cursor.close()

        return nom
    }

    fun updateImage(imageBitmap: Bitmap) {
        val db = writableDatabase

        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        val imageByteArray = stream.toByteArray()

        val contentValues = ContentValues().apply {
            put("image", imageByteArray)
        }

        db.update("profil", contentValues, "id = 1", null)
    }

    fun getImageBitmap(): Bitmap? {
        val db = readableDatabase

        val cursor = db.query("profil", arrayOf("image"), "id = 1", null, null, null, null)
        cursor.moveToFirst()
        val imageIndex = cursor.getColumnIndex("image")
        var imageBitmap: Bitmap? = null
        if (imageIndex >= 0) {
            val imageByteArray = cursor.getBlob(imageIndex)
            imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        }
        cursor.close()

        return imageBitmap
    }
}