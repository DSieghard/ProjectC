package com.sgtech.projectc.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.sgtech.projectc.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.sgtech.projectc.domain.model.Response.Loading
import com.sgtech.projectc.domain.model.Response.Success
import com.sgtech.projectc.domain.repository.OneTapSignInResponse
import com.sgtech.projectc.domain.repository.SignInWithGoogleResponse

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
    val oneTapClient: SignInClient
    ): ViewModel() {

    val isUserAuthenticated get() = repo.isUserAuthenticatedInFirebase

    var oneTapSignInResponse by mutableStateOf<OneTapSignInResponse>(Success(null))
        private set
    var signInWithGoogleResponse by mutableStateOf<SignInWithGoogleResponse>(Success(false))
        private set

    fun oneTapSignIn() = viewModelScope.launch {
        oneTapSignInResponse = Loading
        oneTapSignInResponse = repo.oneTapSignInWithGoogle()
    }

    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        oneTapSignInResponse = Loading
        signInWithGoogleResponse = repo.firebaseSignInWithGoogle(googleCredential)
    }
}