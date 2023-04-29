package ca.uqac.bubble.todolist

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import ca.uqac.bubble.databinding.ActivityToDoListBinding
import ca.uqac.bubble.databinding.PopupTacheBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import ca.uqac.bubble.R
import ca.uqac.bubble.ui.theme.Purple200

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

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.title = "Liste des tâches"
        actionBar?.setBackgroundDrawable(ColorDrawable(Purple200.hashCode()))



        if (SHARED_PREFS.all.keys.isEmpty()){
            tacheAdaptateur = TacheAdaptateur(mutableListOf(), SHARED_PREFS)
        } else {
            var ids = getIds(SHARED_PREFS.all.keys)
            var listeTaches = recupTaches(ids)
            tacheAdaptateur = TacheAdaptateur(listeTaches, SHARED_PREFS)
        }

        binding.toDoList.adapter = tacheAdaptateur
        tacheAdaptateur.notifyDataSetChanged()
        binding.toDoList.layoutManager = LinearLayoutManager(this)

        binding.bAjouterTache.setOnClickListener {
            ajouterTache(tacheAdaptateur)
        }

        binding.bSupprimerTache.setOnClickListener {
            var tachesASupprimer = tacheAdaptateur.supprimerTachesFaites()
            var keys = SHARED_PREFS.all.keys
            for (key in keys) {
                val value = SHARED_PREFS.all[key]
                Log.d("SharedPreferences", "$key: $value")
            }
            for(tache in tachesASupprimer){
                Log.d("SharedPreferences", "id : ${tache.id}")
                supprimerTacheSharedPreferences(tache.id)
            }
            Log.d("SharedPreferences", "===================================")
            keys = SHARED_PREFS.all.keys
            for (key in keys) {
                val value = SHARED_PREFS.all[key]
                Log.d("SharedPreferences", "$key: $value")
            }
        }


        var recyclerView = binding.toDoList
        recyclerView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                recyclerView.postDelayed({
                    recyclerView.smoothScrollToPosition(
                        0
                    )
                }, 100)
            }
        }
        recyclerView.addOnItemTouchListener(
            RecyclerTouchListener(this,
                recyclerView, object : ClickListener {
                    override fun onClick(view: View?, position: Int) {
                    }

                    override fun onLongClick(view: View?, position: Int) {
                        val inflater = LayoutInflater.from(this@ToDoListActivity)
                        val view = inflater.inflate(R.layout.popup_tache, null)
                        val tache = tacheAdaptateur.tacheAt(position)

                        view.findViewById<TextView>(R.id.twAction).text = "Modification de la tâche"

                        view.findViewById<TextView>(R.id.twChoixUrgence).text = tache.urgence.toString()

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

                        var datePickerDialog = DatePickerDialog(this@ToDoListActivity, dateSetListener, year, month, day)

                        view.findViewById<Button>(R.id.bDate).setOnClickListener {
                            datePickerDialog.show()
                        }

                        val nomTache = view.findViewById<EditText>(R.id.etNomTache)
                        nomTache.setText(tache.titre)
                        val categorieTache = view.findViewById<EditText>(R.id.etCategorie)
                        categorieTache.setText(tache.categorie)
                        val twUrgence = view.findViewById<TextView>(R.id.twChoixUrgence)
                        val urgence = view.findViewById<Button>(R.id.bUrgence)
                        when (tache.urgence){
                            0 -> urgence.setText("Pas d'urgence donnée")
                            1 -> urgence.setText("NON URGENT ET NON IMPORTANT")
                            2 -> urgence.setText("NON URGENT ET IMPORTANT")
                            3 -> urgence.setText("URGENT ET NON IMPORTANT")
                            4 -> urgence.setText("URGENT ET IMPORTANT")
                        }

                        val ajouterDialogue = AlertDialog.Builder(this@ToDoListActivity)

                        ajouterDialogue.setView(view)
                        ajouterDialogue.setPositiveButton("Valider"){
                                dialog,_->
                            val nom = nomTache.text.toString()
                            val categorie = categorieTache.text.toString()
                            val date = view.findViewById<Button>(R.id.bDate).text.toString()
                            if (nom.isNotEmpty() && categorie.isNotEmpty()){
                                tache.titre = nom
                                tache.categorie = categorie
                                tache.deadline = recupererDate(date)
                                tache.urgence = twUrgence.text.toString().toInt()
                                tacheAdaptateur.supprimerTacheSharedPreferences(tache.id)
                                stockerTacheSharedPreferences(tache)
                                tacheAdaptateur.notifyItemChanged(position)
                                Toast.makeText(this@ToDoListActivity, "Tache modifiée", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            } else {
                                Toast.makeText(this@ToDoListActivity, "Veuillez remplir tous les champs requis", Toast.LENGTH_SHORT).show()
                            }

                        }
                        ajouterDialogue.setNegativeButton("Annuler"){ dialog,_->
                            dialog.dismiss()
                        }
                        ajouterDialogue.create()
                        ajouterDialogue.show()
                    }
                })
        )


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        val toolbar = menu?.findItem(R.id.toolbar)
        toolbar?.setIcon(R.drawable.icon_filter)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemTitreC -> tacheAdaptateur.triTitreCroissant()
            R.id.itemTitreD -> tacheAdaptateur.triTitreDecroissant()
            R.id.itemCatC -> tacheAdaptateur.triCategorieCroissante()
            R.id.itemCatD -> tacheAdaptateur.triCategorieDecroissante()
            R.id.itemUrgenceC -> tacheAdaptateur.triUrgenceCroissante()
            R.id.itemUrgenceD -> tacheAdaptateur.triUrgenceDecroissante()
            R.id.itemDateC -> tacheAdaptateur.triDeadlineCroissante()
            R.id.itemDateD -> tacheAdaptateur.triDeadlineDecroissante()
        }
        return super.onOptionsItemSelected(item)
    }





    private fun recupTaches(ids: ArrayList<Int>): MutableList<Tache> {
        var taches = mutableListOf<Tache>()

        for(id in ids){
            val nomTache: String = SHARED_PREFS.getString("idNom$id", "").toString()

            val categorieTache: String = SHARED_PREFS.getString("idCategorie$id", "").toString()

            val faite: Boolean = SHARED_PREFS.getBoolean("idFaite$id", false)

            val deadline: LocalDate = recupererDate(SHARED_PREFS.getString("idDate$id", LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))).toString())

            val urgenceTache: Int = SHARED_PREFS.getInt("idUrgence$id", 0)

            taches.add(Tache(id = id,titre = nomTache, categorie = categorieTache, faite = faite, deadline = deadline, urgence = urgenceTache))
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

        view.findViewById<TextView>(R.id.twChoixUrgence).text = "0"

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
                val tache = Tache(titre = nom, categorie = categorie, urgence = urgence.text.toString().toInt(), deadline = recupererDate(date))
                tacheAdaptateur.ajouterTache(tache)
                stockerTacheSharedPreferences(tache)
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

    fun stockerTacheSharedPreferences(tache: Tache) {
        val editor = SHARED_PREFS.edit()
        val id = tache.id
        val idNom = "idNom$id"
        editor.putString(idNom, tache.titre)
        val idCategorie = "idCategorie$id"
        editor.putString(idCategorie, tache.categorie)
        val idFaite = "idFaite$id"
        editor.putBoolean(idFaite, tache.faite)
        val idDate = "idDate$id"
        editor.putString(idDate, tache.deadline.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        val idUrgence = "idUrgence$id"
        editor.putInt(idUrgence, tache.urgence)

        editor.apply()
    }

    fun supprimerTacheSharedPreferences(idTache: Int) {
        val editor = SHARED_PREFS.edit()
        val idNom = "idNom$idTache"
        editor.remove(idNom)
        val idCategorie = "idCategorie$idTache"
        editor.remove(idCategorie)
        val idFaite = "idFaite$idTache"
        editor.remove(idFaite)
        val idDate = "idDate$idTache"
        editor.remove(idDate)
        val idUrgence = "idUrgence$idTache"
        editor.remove(idUrgence)
        editor.apply()
    }
}

interface ClickListener {
    fun onClick(view: View?, position: Int)
    fun onLongClick(view: View?, position: Int)
}

internal class RecyclerTouchListener(
    context: Context?,
    recycleView: RecyclerView,
    clicklistener: ClickListener?
) :
    OnItemTouchListener {
    private val clicklistener: ClickListener?
    private val gestureDetector: GestureDetector

    init {
        this.clicklistener = clicklistener
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recycleView.findChildViewUnder(e.x, e.y)
                if (child != null && clicklistener != null) {
                    clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
            clicklistener?.onClick(child, rv.getChildAdapterPosition(child))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}