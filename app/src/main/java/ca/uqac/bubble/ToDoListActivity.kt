package ca.uqac.bubble

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.SeekBar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.uqac.bubble.databinding.ActivityToDoListBinding
import ca.uqac.bubble.databinding.PopupTacheBinding
import ca.uqac.bubble.databinding.TacheBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ToDoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityToDoListBinding
    private lateinit var bindingTache: PopupTacheBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoListBinding.inflate(layoutInflater)
        bindingTache = PopupTacheBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        var tacheAdaptateur = TacheAdaptateur(mutableListOf())

        binding.toDoList.adapter = tacheAdaptateur
        tacheAdaptateur.notifyDataSetChanged()
        binding.toDoList.layoutManager = LinearLayoutManager(this)

        binding.bAjouterTache.setOnClickListener {
            val popUpView = layoutInflater.inflate(R.layout.popup_tache, null)
            var width = LinearLayout.LayoutParams.WRAP_CONTENT
            var height = LinearLayout.LayoutParams.WRAP_CONTENT
            var focusable = true
            val popupWindow = PopupWindow(popUpView, width, height, focusable)

            popupWindow.showAtLocation(view, Gravity.CENTER,0, 0)

        }

        bindingTache.sbCriticite.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                bindingTache.twUrgenceTache.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        bindingTache.bValiderTache.setOnClickListener{
            val titre = bindingTache.etNomTache.text.toString()
            val categorie = bindingTache.etCategorie.text.toString()
            val date = LocalDate.parse(bindingTache.etDate.text.toString())
            val urgence = bindingTache.sbCriticite.progress
            if(titre.isNotEmpty() && categorie.isNotEmpty()){
                val tache = Tache(titre, categorie,false, date, urgence)
                tacheAdaptateur.ajouterTache(tache)
                bindingTache.etNomTache.text.clear()
                bindingTache.etDate.text.clear()
                bindingTache.etCategorie.text.clear()
                bindingTache.sbCriticite.progress = 0
            }
        }

        binding.bSupprimerTache.setOnClickListener {
            tacheAdaptateur.supprimerTachesFaites()
        }
    }
}