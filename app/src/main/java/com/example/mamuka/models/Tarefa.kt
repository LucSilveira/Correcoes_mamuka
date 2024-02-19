package com.example.mamuka.models

class Tarefa(
    val id : String,
    val status_tarefa : String,
    val nome_tarefa :String,
    val data_conclusao :String,
    val projeto : Projeto,
) {
}