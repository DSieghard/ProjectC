package com.sgtech.projectc.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.sgtech.projectc.domain.model.Response.Loading
import com.sgtech.projectc.domain.model.Response.Success
import com.sgtech.projectc.domain.repository.ProfileRepository
import com.sgtech.projectc.domain.repository.RevokeAccessResponse
import com.sgtech.projectc.domain.repository.SignOutResponse
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepository
): ViewModel() {
    val displayName get() = repo.displayName
    val photoUrl get() = repo.photoUrl

    var signOutResponse by mutableStateOf<SignOutResponse>(Success(false))
        private set
    var revokeAccessResponse by mutableStateOf<RevokeAccessResponse>(Success(false))
        private set

    fun signOut() = viewModelScope.launch {
        signOutResponse = Loading
        signOutResponse = repo.signOut()
    }

    fun revokeAccess() = viewModelScope.launch {
        revokeAccessResponse = Loading
        revokeAccessResponse = repo.revokeAccess()
    }
}