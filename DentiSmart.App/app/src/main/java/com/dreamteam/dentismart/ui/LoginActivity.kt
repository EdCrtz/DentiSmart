package com.dreamteam.dentismart.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.models.LoginResult
import com.dreamteam.dentismart.services.RetrofitService
import com.dreamteam.dentismart.utils.SessionManagement
import com.dreamteam.dentismart.viewmodels.LoginViewModel
import com.muddzdev.styleabletoast.StyleableToast


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#448aff")))
        val boton: Button = findViewById(R.id.buttonLogin)
        val txtUsuario: EditText = findViewById(R.id.txtUser)
        val txtContra: EditText = findViewById(R.id.txtPassword)
        val session = SessionManagement(applicationContext)
        val viewModel = LoginViewModel()
        if (session.isLogin()) {
            RetrofitService.setToken(session.getCurrentUser()!!.token)
            onLogin(session.getCurrentUser())
        }
        boton.setOnClickListener {
            viewModel.onLogin(txtUsuario.text.toString(), txtContra.text.toString())
        }
        val observer: Observer<String> = Observer<String> { message ->
            StyleableToast.makeText(
                applicationContext,
                message,
                R.style.toastBad
            ).show()
        }
        val observerLogin: Observer<LoginResult> =
            Observer<LoginResult> { user ->
                if (user.token.isNotEmpty() && user.role == "Paciente") {
                    RetrofitService.setToken(user.token)
                    session.createLoginSession(user)
                    StyleableToast.makeText(
                        applicationContext,
                        "¡Bienvenido!\n${user.role}",
                        R.style.toastGood
                    ).show()
                    onLogin(user)
                }
                else{
                    StyleableToast.makeText(
                        applicationContext,
                        "¡Lo sentimos aun solo es disponible para pacientes!",
                        R.style.toastWarning
                    ).show()
                }
            }
        viewModel.messageResponse.observe(this, observer)
        viewModel.getUserLoged().observe(this, observerLogin)

    }

    private fun onLogin(user: LoginResult?) {
        val intent = Intent(
            this,
            HomeActivity::class.java
        )
        startActivity(intent)
        finish()
    }
}