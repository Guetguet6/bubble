package ca.uqac.bubble

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.uqac.bubble.databinding.ActivityToDoListBinding
import ca.uqac.bubble.databinding.PopupTacheBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ToDoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityToDoListBinding
    private lateinit var bindingTache: PopupTacheBinding
    private lateinit var SHARED_PREFS: SharedPreferences
    private lateinit var tacheAdaptateur: TacheAdaptateur


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoListBinding.inflate(layoutInflater)
        bindingTache = PopupTacheBinding.inflate(layoutInflater)
        SHARED_PREFS = this.getSharedPreferences("listeTACHES", MODE_PRIVATE)

        val view = binding.root
        setContentView(view)

        if (SHARED_PREFS.all.keys.isEmpty()){
            tacheAdaptateur = TacheAdaptateur(mutableListOf())
        } else {
            var ids = getIds(SHARED_PREFS.all.keys)
            var listeTaches = recupTaches(ids)
            tacheAdaptateur = TacheAdaptateur(listeTaches)
        }

        binding.toDoList.adapter = tacheAdaptateur
        tacheAdaptateur.notifyDataSetChanged()
        binding.toDoList.layoutManager = LinearLayoutManager(this)

        binding.bAjouterTache.setOnClickListener {
            ajouterTache(tacheAdaptateur)
        }

        binding.bSupprimerTache.setOnClickListener {
            tacheAdaptateur.supprimerTachesFaites()
        }
    }




    private fun recupTaches(ids: ArrayList<Int>): MutableList<Tache> {
        var taches = mutableListOf<Tache>()
        var editor = SHARED_PREFS.edit()

        for(id in ids){
            var nomTache: String = SHARED_PREFS.getString("idNom$id", "").toString()
            editor.remove("idNom$id")
            editor.apply()

            var categorieTache: String = SHARED_PREFS.getString("idCategorie$id", "").toString()
            editor.remove("idCategorie$id")
            editor.apply()

            var faite: Boolean = SHARED_PREFS.getBoolean("idFaite$id", false)
            editor.remove("idFaite$id")
            editor.apply()

            var deadline: LocalDate = recupererDate(SHARED_PREFS.getString("idDate$id", LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))).toString())
            editor.remove("idDate$id")
            editor.apply()

            var urgenceTache: Int = SHARED_PREFS.getInt("idUrgence$id", 0)
            editor.remove("idUrgence$id")
            editor.apply()

            taches.add(Tache(nomTache, categorieTache, faite, deadline, urgenceTache))
        }

        return taches
    }

    private fun getIds(keys: MutableSet<String>): ArrayList<Int> {
        var ids = kotlin.collections.ArrayList<Int>()

        for(key in keys) {
            if(key.contains("idNom")){
                ids.add(key.substring(5).toInt())
            }
        }

        return ids
    }


    private fun choisirUrgence(parent: View){
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.matrice_eisenhower, null)

        var twChoixUrgence = parent.findViewById<TextView>(R.id.twChoixUrgence)
        var bChoix = parent.findViewById<Button>(R.id.bUrgence)
        var choix = view.findViewById<TextView>(R.id.twChoix)

        val bUI = view.findViewById<Button>(R.id.buttonUI)
        bUI.setOnClickListener {
            twChoixUrgence.text = "4"
            choix.text = "Vous avez choisi : Urgent et Important"
            bChoix.text = "Urgent et Important"

        }
        val bUN = view.findViewById<Button>(R.id.buttonUN)
        bUN.setOnClickListener {
            twChoixUrgence.text = "3"
            choix.text = "Vous avez choisi : Urgent et Non Important"
            bChoix.text = "Urgent et Non Important"
        }
        val bNI = view.findViewById<Button>(R.id.buttonNI)
        bNI.setOnClickListener {
            twChoixUrgence.text = "2"
            choix.text = "Vous avez choisi : Non Urgent et Important"
            bChoix.text = "Non Urgent et Important"
        }
        val bNN = view.findViewById<Button>(R.id.buttonNN)
        bNN.setOnClickListener {
            twChoixUrgence.text = "1"
            choix.text = "Vous avez choisi : Non Urgent et Non Important"
            bChoix.text = "Non Urgent et Non Important"
        }

        val ajouterDialogue = AlertDialog.Builder(this)

        ajouterDialogue.setView(view)
        ajouterDialogue.setPositiveButton("Valider"){
                dialog,_->
            dialog.dismiss()
        }
        ajouterDialogue.setNegativeButton("Annuler"){ dialog,_->
            dialog.dismiss()
        }
        ajouterDialogue.create()
        ajouterDialogue.show()
    }

    private fun ajouterTache(tacheAdaptateur: TacheAdaptateur){
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.popup_tache, null)

        view.findViewById<Button>(R.id.bUrgence).setOnClickListener {
            choisirUrgence(view)
        }

        var cal = Calendar.getInstance()

        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.FRANCE)
        var dateSetListener = DatePickerDialog.OnDateSetListener { _ , year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            view.findViewById<Button>(R.id.bDate).text = sdf.format(cal.time)
        }
        var year = cal.get(Calendar.YEAR)
        var month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DAY_OF_MONTH)
        view.findViewById<Button>(R.id.bDate).text = sdf.format(cal.time)

        var datePickerDialog = DatePickerDialog(this, dateSetListener, year, month, day)

        view.findViewById<Button>(R.id.bDate).setOnClickListener {
            datePickerDialog.show()
        }

        val nomTache = view.findViewById<EditText>(R.id.etNomTache)
        val categorieTache = view.findViewById<EditText>(R.id.etCategorie)
        val urgence = view.findViewById<TextView>(R.id.twChoixUrgence)

        val ajouterDialogue = AlertDialog.Builder(this)

        ajouterDialogue.setView(view)
        ajouterDialogue.setPositiveButton("Valider"){
            dialog,_->
            val nom = nomTache.text.toString()
            val categorie = categorieTache.text.toString()
            val date = view.findViewById<Button>(R.id.bDate).text.toString()
            if (nom.isNotEmpty() && categorie.isNotEmpty()){
                tacheAdaptateur.ajouterTache(Tache(nom, categorie, urgence = urgence.text.toString().toInt(), deadline = recupererDate(date)))
                tacheAdaptateur.notifyDataSetChanged()
                Toast.makeText(this, "Tache ajoutée", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs requis", Toast.LENGTH_SHORT).show()
            }

        }
        ajouterDialogue.setNegativeButton("Annuler"){ dialog,_->
            dialog.dismiss()
        }
        ajouterDialogue.create()
        ajouterDialogue.show()
    }

    fun recupererDate(date: String): LocalDate {
        var localDate: LocalDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return localDate
    }

    fun stockerTaches(tacheAdaptateur: TacheAdaptateur) {
        var taches = tacheAdaptateur.recupererTaches()
        var editor = SHARED_PREFS.edit()
        var idNom: String
        var idCategorie: String
        var idUrgence: String
        var idDate: String
        var idFaite: String

        for(i in 0 until taches.size){
            idNom = "idNom$i"
            editor.putString(idNom, taches[i].titre)
            idCategorie = "idCategorie$i"
            editor.putString(idCategorie, taches[i].categorie)
            idFaite = "idFaite$i"
            editor.putBoolean(idFaite, taches[i].faite)
            idDate = "idDate$i"
            editor.putString(idDate, taches[i].deadline.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            idUrgence = "idUrgence$i"
            editor.putInt(idUrgence, taches[i].urgence)
        }

        editor.apply()
    }

    override fun onPause() {
        stockerTaches(tacheAdaptateur)
        super.onPause()
    }
}