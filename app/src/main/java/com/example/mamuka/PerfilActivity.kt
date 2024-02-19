package com.example.mamuka

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.mamuka.api.Endpoint
import com.example.mamuka.api.RetrofitConfig
import com.example.mamuka.models.AlterarSenha
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilActivity : AppCompatActivity() {
    private val clienteRetrofit = RetrofitConfig.obterInstanciaRetrofit()

    private val endpoints = clienteRetrofit.create(Endpoint::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val btnEntrar = findViewById<Button>(R.id.trocar_foto)
        btnEntrar.setOnClickListener {
            alterarSenha()
        }

        val btnVoltar = findViewById<LinearLayout>(R.id.backPerfil)
        btnVoltar.setOnClickListener(){

            val mainIntent = Intent(this@PerfilActivity, MainActivity::class.java)

            startActivity(mainIntent)
        }
    }

    private fun alterarSenha() {
        val novaSenha = findViewById<EditText>(R.id.campo_nova_senha)
        val confirmarSenha = findViewById<EditText>(R.id.campo_confirmar_senha)

        val valorSenha = novaSenha.text.toString()
        val valorConfirmacao = confirmarSenha.text.toString()

        if (valorSenha == valorConfirmacao) {
            val objAlterar = AlterarSenha(valorSenha);

            val sharedPreferences = getSharedPreferences("idUsuario", Context.MODE_PRIVATE)

            val idUsuario = sharedPreferences.getString("idUsuario", "")


            endpoints.alterarSenha(objAlterar, idUsuario.toString()).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    when (response.code()) {
                        200 -> {
                            tratarFalhaNaRequisicao("Senha alterada")
                        }

                        400 -> {
                            tratarFalhaNaRequisicao("E-mail e/ou senha inválidos.")
                        }

                        else -> {
                            tratarFalhaNaRequisicao("Falha ao se logar! ${idUsuario}")
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    tratarFalhaNaRequisicao("Falha ao atualizar senha")
                }

            })


        } else {
            tratarFalhaNaRequisicao("Senhas incompátiveis")
        }
    }

    private fun tratarFalhaNaRequisicao(mensagemErro: String) {
        Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show()
    }
}
