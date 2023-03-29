package ca.uqac.bubble

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.compose.ui.platform.LocalContext
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException


class TacheAdaptateur(
    private var taches: MutableList<Tache>,

) : RecyclerView.Adapter<TacheAdaptateur.TacheViewHolder>(){

    class TacheViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var nomTache: TextView
        var boutonTacheFaite: CheckBox
        var boutonSupprimerTache: ImageButton
        var categorieTache: TextView
        var urgenceTache: TextView


        init {
            nomTache = itemView.findViewById(R.id.nomTache)
            boutonTacheFaite = itemView.findViewById(R.id.boutonTacheFaite)
            boutonSupprimerTache = itemView.findViewById(R.id.boutonSupprimerTache)
            categorieTache = itemView.findViewById(R.id.twCategorie)
            urgenceTache = itemView.findViewById(R.id.twUrgence)
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

    private fun toggleStrikeThrough(nomTache: TextView, categorieTache: TextView, urgenceTache: TextView, isChecked: Boolean){
        if(isChecked) {
            nomTache.paintFlags = nomTache.paintFlags or STRIKE_THRU_TEXT_FLAG
            categorieTache.paintFlags = categorieTache.paintFlags or STRIKE_THRU_TEXT_FLAG
            urgenceTache.paintFlags = urgenceTache.paintFlags or STRIKE_THRU_TEXT_FLAG

        } else {
            nomTache.paintFlags = nomTache.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            categorieTache.paintFlags = categorieTache.paintFlags or STRIKE_THRU_TEXT_FLAG.inv()
            urgenceTache.paintFlags = urgenceTache.paintFlags or STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TacheViewHolder, position: Int) {
        var tacheActuelle: Tache = taches[position]
        holder.nomTache.text = tacheActuelle.titre
        holder.categorieTache.text = tacheActuelle.categorie
        holder.urgenceTache.text = tacheActuelle.urgence.toString()
        holder.boutonTacheFaite.isChecked = tacheActuelle.faite

        toggleStrikeThrough(holder.nomTache, holder.categorieTache, holder.urgenceTache, holder.boutonTacheFaite.isChecked)
        holder.boutonTacheFaite.setOnCheckedChangeListener { _ , isChecked ->
            toggleStrikeThrough(holder.nomTache, holder.categorieTache, holder.urgenceTache, holder.boutonTacheFaite.isChecked)
            tacheActuelle.faite = holder.boutonTacheFaite.isChecked
        }

        holder.boutonSupprimerTache.setOnClickListener {
            supprimerTache(tacheActuelle, position)
        }
    }

    override fun getItemCount(): Int {
        return taches.size
    }


}