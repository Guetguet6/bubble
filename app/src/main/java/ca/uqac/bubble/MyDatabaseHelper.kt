package ca.uqac.bubble

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ca.uqac.bubble.Calendrier.Event
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.util.ArrayList

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "bubble.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE profil (id INTEGER PRIMARY KEY, nom TEXT, image BLOB)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS profil"

        private const val SQL_CREATE_CALENDAR_ENTRIES =
            "CREATE TABLE calendrier (id INTEGER PRIMARY KEY, nom TEXT, date LocalDate, time LocalTime)"

        private const val SQL_DELETE_CALENDAR_ENTRIES = "DROP TABLE IF EXISTS calendrier"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
        db?.execSQL(SQL_CREATE_CALENDAR_ENTRIES)

        val contentValues = ContentValues().apply {
            put("nom", "Utilisateur")
        }

        db?.insert("profil", null, contentValues)

        val values = ContentValues().apply {
            put("id", "1")
            put("nom", "Ma réunion")
            put("date", "2023-05-01")
            put("time", "14:30:00")
        }
        db?.insert("calendrier", null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        db?.execSQL(SQL_DELETE_CALENDAR_ENTRIES)
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

    @SuppressLint("Range")
    fun getAllEvents(): List<Event> {
        val eventsList = mutableListOf<Event>()
        val selectQuery = "SELECT * FROM calendrier"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val nom = cursor.getString(cursor.getColumnIndex("nom"))
                val date = LocalDate.parse(cursor.getString(cursor.getColumnIndex("date")))
                val time = LocalTime.parse(cursor.getString(cursor.getColumnIndex("time")))
                eventsList.add(Event(nom, date, time))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return eventsList
    }

    @SuppressLint("Range")
    fun getEventById(id: Int): Event? {
        val db = this.readableDatabase
        val cursor = db.query(
            "calendrier", arrayOf("id", "nom", "date", "time"), "id = ?",
            arrayOf(id.toString()), null, null, null, null
        )
        var event: Event? = null
        if (cursor.moveToFirst()) {
            val nom = cursor.getString(cursor.getColumnIndex("nom"))
            val date = LocalDate.parse(cursor.getString(cursor.getColumnIndex("date")))
            val time = LocalTime.parse(cursor.getString(cursor.getColumnIndex("time")))
            event = Event(nom, date, time)
        }
        cursor.close()
        db.close()
        return event
    }

    @SuppressLint("Range")
    fun getEventsByDate(date: LocalDate): ArrayList<Event> {
        val eventsList = ArrayList<Event>()
        val selectQuery = "SELECT * FROM calendrier WHERE date = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(date.toString()))
        if (cursor.moveToFirst()) {
            do {
                val nom = cursor.getString(cursor.getColumnIndex("nom"))
                val time = LocalTime.parse(cursor.getString(cursor.getColumnIndex("time")))
                eventsList.add(Event(nom, date, time))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return eventsList
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