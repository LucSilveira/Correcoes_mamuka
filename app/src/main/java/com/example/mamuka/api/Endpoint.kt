package com.example.mamuka.api

import com.example.mamuka.models.AlterarSenha
import com.example.mamuka.models.Login
import com.example.mamuka.models.Tarefa
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.*

interface Endpoint {

    @GET("tarefas")
    fun listarTarefas() : Call<List<Tarefa>>

    @POST("login")
    fun login (@Body usuario : Login) : Call<JsonObject>

    @PUT("usuarios/alterarsenha/{idUsuario}")
    fun alterarSenha(@Body alterar : AlterarSenha, @Path(value = "idUsuario", encoded = true) idUsuario: String) : Call<JsonObject>
}