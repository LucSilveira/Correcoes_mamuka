package com.example.mamuka

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mamuka.api.Endpoint
import com.example.mamuka.api.RetrofitConfig
import com.example.mamuka.models.Login
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.StandardCharsets
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val clienteRetrofit = RetrofitConfig.obterInstanciaRetrofit()

    private val endpoints = clienteRetrofit.create(Endpoint::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences("idUsuario", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.remove("idUsuario")

        editor.apply()

        //setOnClickListener é um ouvinte de clique
        //Ou seja, quando clicar no botão entrar irá cair nesse bloco
        val btnEntrar = findViewById<Button>(R.id.btn_entrar)
        btnEntrar.setOnClickListener {
            autenticarUsuario()

            val mainIntent = Intent(this@LoginActivity, PerfilActivity::class.java)

            startActivity(mainIntent)
        }
    }

    private fun autenticarUsuario(){

        val idEmail = findViewById<EditText>(R.id.campo_nova_senha)
        val idSenha = findViewById<EditText>(R.id.campo_confirmar_senha)

        val emailDigitado = idEmail.text.toString()
        val senhaDigitado = idSenha.text.toString()

        val usuario = Login(emailDigitado, senhaDigitado)

        endpoints.login( usuario ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                when(response.code()){
                    200 -> {
                        val idUsuario = decodificarToken( response.body().toString() )

                        //direcionando o usuário para tela lista de serviços
                        val mainIntent = Intent(this@LoginActivity, PerfilActivity::class.java)

                        startActivity(mainIntent)

                        finish()
                    }
                    403 -> { tratarFalhaNaAutenticacao("E-mail e/ou senha inválidos.") }
                    else -> { tratarFalhaNaAutenticacao("Falha ao se logar!") }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                tratarFalhaNaAutenticacao("Falha ao tentar se logar!")
            }

        })
    }

    private fun tratarFalhaNaAutenticacao(mensagemErro: String){
        Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show()
    }

    private fun decodificarToken(token: String): Any {
        val partes = token.split(".")
        val payloadBase64 = partes[1]

        val payloadBytes = Base64.getUrlDecoder().decode(payloadBase64)
        val payloadJson = String(payloadBytes, StandardCharsets.UTF_8)

        val json = JSONObject(payloadJson)

        // Adicionando os dados do usuario no sharedPreferences
        val sharedPreferences = getSharedPreferences("idUsuario", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.putString("idUsuario", json["idUsuario"].toString())
        editor.putString("nomeUsuario", json["nomeUsuario"].toString())

        editor.apply()

        return true;
    }
}
