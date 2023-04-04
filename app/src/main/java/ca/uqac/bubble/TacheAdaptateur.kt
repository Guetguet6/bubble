package ca.uqac.bubble

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.Period
import java.util.*


class TacheAdaptateur(
    private var taches: MutableList<Tache>,
    var SHARED_PREFS: SharedPreferences,
    private var tachesFiltrees: MutableList<Tache> = mutableListOf(),

    ) : RecyclerView.Adapter<TacheAdaptateur.TacheViewHolder>(), Filterable {

    init {
        tachesFiltrees = taches
    }

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


    fun ajouterTache(tache: Tache) {
        taches.add(tache)
        notifyItemInserted(taches.size - 1)
    }

    fun supprimerTache(tache: Tache, position: Int){
        taches.remove(tache)
        notifyItemRemoved(position)
        notifyItemRangeChanged(0, itemCount)
    }

    fun supprimerTachesFaites() {
        taches.removeAll { tache ->
            tache.faite
        }
        notifyDataSetChanged()
    }

    fun triTitreCroissant() {
        taches.sortBy { it.titre }
        notifyDataSetChanged()
    }

    fun triTitreDecroissant() {
        taches.sortByDescending { it.titre }
        notifyDataSetChanged()
    }

    fun triCategorieCroissante() {
        taches.sortBy { it.categorie }
        notifyDataSetChanged()
    }

    fun triCategorieDecroissante() {
        taches.sortByDescending { it.categorie }
        notifyDataSetChanged()
    }

    fun triUrgenceCroissante() {
        taches.sortBy { it.urgence }
        notifyDataSetChanged()
    }

    fun triUrgenceDecroissante() {
        taches.sortByDescending { it.urgence }
        notifyDataSetChanged()
    }

    fun triTacheFaite() {
        taches.sortBy { it.faite }
        notifyDataSetChanged()
    }

    fun triTacheNonFaite() {
        taches.sortByDescending { it.faite }
        notifyDataSetChanged()
    }

    fun triDeadlineCroissante() {
        taches.sortBy { it.deadline }
        notifyDataSetChanged()
    }

    fun triDeadlineDecroissante() {
        taches.sortByDescending { it.deadline }
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
        var tacheActuelle: Tache = tachesFiltrees[position]
        holder.nomTache.text = tacheActuelle.titre
        holder.categorieTache.text = tacheActuelle.categorie
        var tempsRestant = getTempsRestant(tacheActuelle.deadline)
        holder.dateTache.text = tempsRestant
        holder.boutonTacheFaite.isChecked = tacheActuelle.faite
        var color = couleurTache(tacheActuelle.urgence)
        holder.itemView.setBackgroundColor(color)
        if (tacheActuelle.urgence == 4) {
            holder.nomTache.setTextColor(Color.WHITE)
            holder.categorieTache.setTextColor(Color.WHITE)
            holder.dateTache.setTextColor(Color.WHITE)
        }

        toggleStrikeThrough(holder.nomTache, holder.categorieTache, holder.boutonTacheFaite.isChecked)
        holder.boutonTacheFaite.setOnCheckedChangeListener { _ , isChecked ->
            toggleStrikeThrough(holder.nomTache, holder.categorieTache, holder.boutonTacheFaite.isChecked)
            tacheActuelle.faite = holder.boutonTacheFaite.isChecked
        }

        holder.boutonSupprimerTache.setOnClickListener {
            supprimerTacheSharedPreferences(tacheActuelle.id)
            supprimerTache(tacheActuelle, position)
        }

    }

    fun supprimerTacheSharedPreferences(idTache: Int) {
        val editor = SHARED_PREFS.edit()
        val idNom = "idNom$idTache"
        editor.remove(idNom)
        editor.apply()
        val idCategorie = "idCategorie$idTache"
        editor.remove(idCategorie)
        editor.apply()
        val idFaite = "idFaite$idTache"
        editor.remove(idFaite)
        editor.apply()
        val idDate = "idDate$idTache"
        editor.remove(idDate)
        editor.apply()
        val idUrgence = "idUrgence$idTache"
        editor.remove(idUrgence)
        editor.apply()
    }


    private fun getTempsRestant(deadline: LocalDate): String {
        val now = LocalDate.now()
        val period = Period.between(now, deadline)

        var tempsRestant = "Temps restant : "
        val ans = period.years
        val mois = period.months
        val jours = period.days

        if (ans >= 0 && mois >= 0 && jours >= 0) {
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
        } else {
            tempsRestant = "Date limite dépassée !"
        }

        return tempsRestant
    }

    private fun couleurTache(urgence: Int): Int {
        if (urgence == 1){
            return android.graphics.Color.parseColor("#90E0EF")
        } else if (urgence == 2) {
            return android.graphics.Color.parseColor("#48CAE4")
        } else if (urgence == 3) {
            return android.graphics.Color.parseColor("#0077B6")
        } else if (urgence == 4) {
            return android.graphics.Color.parseColor("#023E8A")
        } else {
            return android.graphics.Color.parseColor("#CAF0F8")
        }

    }

    override fun getItemCount(): Int {
        return tachesFiltrees.size
    }

    fun tacheAt(position: Int): Tache {
        return taches[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    tachesFiltrees = taches
                } else {
                    var listeFiltree = ArrayList<Tache>()
                    for (tache in taches) {
                        if (tache.titre.lowercase().contains(charString)) {
                            listeFiltree.add(tache)
                        }
                    }
                    tachesFiltrees = listeFiltree
                }
                val filterResults = FilterResults()
                filterResults.values = tachesFiltrees
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                tachesFiltrees = results.values as MutableList<Tache>
                notifyDataSetChanged()
            }
        }
    }


}