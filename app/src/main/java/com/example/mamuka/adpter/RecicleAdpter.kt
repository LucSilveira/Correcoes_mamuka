package com.example.mamuka.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mamuka.R
import com.example.mamuka.models.Tarefa
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecicleAdpter(
    private val context: Context,
    private val listaTarefa: List<Tarefa>
) : RecyclerView.Adapter<RecicleAdpter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        //Essa função é responsável por chamar e atribuir valores para as views do item da RecyclerView
        fun vincularDadosNoItem(tarefa: Tarefa) {

            val txtNomeProjeto = itemView.findViewById<TextView>(R.id.nomeProjeto)
            txtNomeProjeto.text = tarefa.projeto.nome_projeto

            val txtNomeTarefa = itemView.findViewById<TextView>(R.id.nomeTarefa)
            txtNomeTarefa.text = tarefa.nome_tarefa

            val txtStatusTarefa = itemView.findViewById<TextView>(R.id.statusTarefa)
            txtStatusTarefa.text = tarefa.status_tarefa

            val txtCronograma = itemView.findViewById<TextView>(R.id.cronograma)

            val localDateTime: LocalDateTime = LocalDateTime.parse( "${tarefa.data_conclusao}T00:00:00")
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val output: String = formatter.format(localDateTime)
            txtCronograma.text = output
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecicleAdpter.ViewHolder {
        val inflater = LayoutInflater.from(context);

        val view = inflater.inflate(R.layout.card_tarefa, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecicleAdpter.ViewHolder, position: Int) {
        val itemTarefa = listaTarefa[position]

        holder.vincularDadosNoItem(itemTarefa)
    }

    override fun getItemCount(): Int {
        return listaTarefa.size
    }
}