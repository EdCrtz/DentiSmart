package com.dreamteam.dentismart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val boton:Button = findViewById(R.id.boton)
        val txtUsuario:EditText = findViewById(R.id.txtUsuario)
        val txtContra:EditText = findViewById(R.id.txtPasword)
        boton.setOnClickListener {
            Toast.makeText(this,"Usuario ${txtUsuario.text}  contraseña ${txtContra.text}",Toast.LENGTH_SHORT).show()
        }

    }
}