package ca.uqac.bubble.todolist

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.recyclerview.widget.RecyclerView
import ca.uqac.bubble.R
import java.time.LocalDate
import java.time.Period


class TacheAdaptateur(
    private var taches: MutableList<Tache>,

    ) : RecyclerView.Adapter<TacheAdaptateur.TacheViewHolder>(){

    class TacheViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var nomTache: TextView
        var boutonTacheFaite: CheckBox
        var boutonSupprimerTache: ImageButton
        var categorieTache: TextView
        var dateTache: TextView


        init {
            nomTache = itemView.findViewById(R.id.nomTache)
            boutonTacheFaite = itemView.findViewById(R.id.boutonTacheFaite)
            boutonSupprimerTache = itemView.findViewById(R.id.boutonSupprimerTache)
            categorieTache = itemView.findViewById(R.id.twCategorie)
            dateTache = itemView.findViewById(R.id.twDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TacheViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.tache,
            parent,
            false,
        )
        return TacheViewHolder(view)
    }

    fun recupererTaches(): MutableList<Tache> {
        return taches

    }

    fun ajouterTache(tache: Tache) {
        taches.add(tache)
        notifyItemInserted(taches.size - 1)
    }

    fun supprimerTache(tache: Tache, position: Int){
        taches.remove(tache)
        notifyItemRemoved(position)

    }

    fun supprimerTachesFaites() {
        taches.removeAll { tache ->
            tache.faite
        }
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(nomTache: TextView, categorieTache: TextView, isChecked: Boolean){
        if(isChecked) {
            nomTache.paintFlags = nomTache.paintFlags or STRIKE_THRU_TEXT_FLAG
            categorieTache.paintFlags = categorieTache.paintFlags or STRIKE_THRU_TEXT_FLAG

        } else {
            nomTache.paintFlags = nomTache.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            categorieTache.paintFlags = categorieTache.paintFlags or STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TacheViewHolder, position: Int) {
        var tacheActuelle: Tache = taches[position]
        holder.nomTache.text = tacheActuelle.titre
        holder.categorieTache.text = tacheActuelle.categorie
        var tempsRestant = getTempsRestant(tacheActuelle.deadline)
        holder.dateTache.text = tempsRestant
        holder.boutonTacheFaite.isChecked = tacheActuelle.faite
        var color = couleurTache(tacheActuelle.urgence)
        holder.itemView.setBackgroundColor(color)

        toggleStrikeThrough(holder.nomTache, holder.categorieTache, holder.boutonTacheFaite.isChecked)
        holder.boutonTacheFaite.setOnCheckedChangeListener { _ , isChecked ->
            toggleStrikeThrough(holder.nomTache, holder.categorieTache, holder.boutonTacheFaite.isChecked)
            tacheActuelle.faite = holder.boutonTacheFaite.isChecked
        }

        holder.boutonSupprimerTache.setOnClickListener {
            supprimerTache(tacheActuelle, position)
        }
    }

    private fun getTempsRestant(deadline: LocalDate): String {
        val now = LocalDate.now()
        val period = Period.between(now, deadline)

        var tempsRestant = "Temps restant : "
        val ans = period.years
        val mois = period.months
        val jours = period.days

        if (ans != 0) {
            if (ans == 1) {
                tempsRestant += "$ans an"
            } else {
                tempsRestant += "$ans ans"
            }
        }
        if (mois != 0) {
            if (ans != 0) {
                tempsRestant += " et $mois mois"
            } else {
                tempsRestant += "$mois mois"
            }
        }
        if (jours != 0) {
            if (mois != 0 || ans != 0) {
                if (jours == 1) {
                    tempsRestant += " et $jours jour"
                } else {
                    tempsRestant += " et $jours jours"
                }
            } else {
                if (jours == 1) {
                    tempsRestant += "$jours jour"
                } else {
                    tempsRestant += "$jours jours"
                }
            }
        }

        return tempsRestant
    }

    private fun couleurTache(urgence: Int): Int {
        if (urgence == 1){
            return Color.Green.toArgb()
        } else if (urgence == 2) {
            return Color.Yellow.toArgb()
        } else if (urgence == 3) {
            return Color.Magenta.toArgb()
        } else if (urgence == 4) {
            return Color.Red.toArgb()
        } else {
            return Color.White.toArgb()
        }

    }

    override fun getItemCount(): Int {
        return taches.size
    }


}