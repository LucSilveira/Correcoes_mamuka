package com.example.mamuka

import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mamuka.adpter.RecicleAdpter
import com.example.mamuka.api.Endpoint
import com.example.mamuka.api.RetrofitConfig
import com.example.mamuka.models.Tarefa
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class MainActivity : AppCompatActivity() {

    private val clienteRetrofit = RetrofitConfig.obterInstanciaRetrofit()
    private val endpoints = clienteRetrofit.create(Endpoint::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("idUsuario", Context.MODE_PRIVATE)
        val nomeUsuario = sharedPreferences.getString("nomeUsuario", "")
        findViewById<TextView>(R.id.nomeUsuario).text = nomeUsuario

        var reciclerView = findViewById<RecyclerView>(R.id.recyclerServicos)

        reciclerView.layoutManager = LinearLayoutManager( this )

        endpoints.listarTarefas().enqueue(object : Callback<List<Tarefa>> {
            override fun onResponse(call: Call<List<Tarefa>>, response: Response<List<Tarefa>>) {
                val tarefas = response.body()

                reciclerView.adapter = tarefas?.let { RecicleAdpter(this@MainActivity, it) }
            }

            override fun onFailure(call: Call<List<Tarefa>>, t: Throwable) {
                println("Falha na requisição: ${t.message}")
            }

        })

        val exitLink = findViewById<ImageView>(R.id.exitApp)
        exitLink.setOnClickListener(){
            val mainIntent = Intent(this@MainActivity, LoginActivity::class.java)

            startActivity(mainIntent)
        }


        val perfilLink = findViewById<LinearLayout>(R.id.perfilUsuario)
        perfilLink.setOnClickListener(){
            val mainIntent = Intent(this@MainActivity, PerfilActivity::class.java)

            startActivity(mainIntent)
        }
    }
}