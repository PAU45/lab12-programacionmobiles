package com.melendez.paulo.poketinder.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melendez.paulo.poketinder.data.database.SharedPreferencesRepository

class LoginViewModel(val context: Context) : ViewModel() {

    val inputsError = MutableLiveData<Boolean>()
    val authError = MutableLiveData<Boolean>()
    val loginSuccess = MutableLiveData<Boolean>()
    val registerSuccess = MutableLiveData<Boolean>()
    val passwordMismatchError = MutableLiveData<Boolean>()

    private var sharedPreferencesRepository: SharedPreferencesRepository =
        SharedPreferencesRepository().also {
            it.setSharedPreference(context)
        }

    fun validateInputs(email: String, password: String) {
        if (isEmptyInputs(email, password)) {
            inputsError.postValue(true)
            return
        }

        val emailSharedPreferences = sharedPreferencesRepository.getUserEmail()
        val passwordPreferences = sharedPreferencesRepository.getUserPassword()

        if (emailSharedPreferences == email && passwordPreferences == password) {
            loginSuccess.postValue(true)
        } else {
            authError.postValue(true)
        }
    }

    fun validateRegistration(email: String, password: String, confirmPassword: String) {
        if (isEmptyInputs(email, password) || confirmPassword.isEmpty()) {
            inputsError.postValue(true)
            return
        }

        if (!isValidEmail(email)) {
            inputsError.postValue(true) // O puedes crear un LiveData específico para errores de email
            return
        }

        if (password.length < 8) {
            inputsError.postValue(true) // O similar para un LiveData específico
            return
        }

        if (password != confirmPassword) {
            passwordMismatchError.postValue(true)
            return
        }

        // Registro exitoso
        sharedPreferencesRepository.saveUserEmail(email)
        sharedPreferencesRepository.saveUserPassword(password)
        registerSuccess.postValue(true)
    }

    private fun isEmptyInputs(vararg inputs: String) = inputs.any { it.isEmpty() }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}