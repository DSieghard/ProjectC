package com.sgtech.projectc.data.repository

import androidx.compose.runtime.snapshots.SnapshotApplyResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.sgtech.projectc.core.Constants.CREATED_AT
import com.sgtech.projectc.core.Constants.DISPLAY_NAME
import com.sgtech.projectc.core.Constants.EMAIL
import com.sgtech.projectc.core.Constants.PHOTO_URL
import com.sgtech.projectc.core.Constants.SIGN_IN_REQUEST
import com.sgtech.projectc.core.Constants.SIGN_UP_REQUEST
import com.sgtech.projectc.core.Constants.USERS
import com.sgtech.projectc.domain.model.Response.Success
import com.sgtech.projectc.domain.model.Response.Failure
import com.sgtech.projectc.domain.repository.AuthRepository
import com.sgtech.projectc.domain.repository.OneTapSignInResponse
import com.sgtech.projectc.domain.repository.SignInWithGoogleResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private var db: FirebaseFirestore
) : AuthRepository {
    override val isUserAuthenticatedInFirebase = auth.currentUser != null

    override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Success(signInResult)
        } catch (e: Exception) {
           Failure(e)
        }
    }

    override suspend fun firebaseSignInWithGoogle(
        googleCredential: AuthCredential
    ): SignInWithGoogleResponse {
        return try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if(isNewUser) {
                addUserToFirestore()
            }
            Success(true)
        } catch(e: Exception) {
            Failure(e)
        }
    }
    private suspend fun addUserToFirestore() {
        auth.currentUser?.apply{
            val user = toUser()
            db.collection(USERS).document(uid).set(user).await()
        }
    }

    fun FirebaseUser.toUser() = mapOf(
        DISPLAY_NAME to displayName,
        EMAIL to email,
        PHOTO_URL to photoUrl?.toString(),
        CREATED_AT to serverTimestamp()
    )
}