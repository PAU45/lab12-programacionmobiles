package com.melendez.paulo.poketinder
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.melendez.paulo.poketinder.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = LoginViewModel(this)
        observeRegisterValues()

        binding.btnRegister.setOnClickListener {
            loginViewModel.validateRegistration(
                email = binding.edtEmail.text.toString(),
                password = binding.edtPassword.text.toString(),
                confirmPassword = binding.edtPassword2.text.toString() // Cambiado a edtPassword2
            )
        }

        binding.btnBackClose.setOnClickListener {
            finish() // Regresa a la pantalla anterior
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java)) // Navegar a LoginActivity
        }
    }

    private fun observeRegisterValues() {
        loginViewModel.inputsError.observe(this) {
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show()
        }
        loginViewModel.passwordMismatchError.observe(this) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        }
        loginViewModel.registerSuccess.observe(this) {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // Termina RegisterActivity para evitar volver atrás
            }
        }
    }
}