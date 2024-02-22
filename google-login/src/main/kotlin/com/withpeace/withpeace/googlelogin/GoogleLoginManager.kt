package com.withpeace.withpeace.googlelogin

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GoogleLoginManager(val context: Context) {
    private val credentialManager = CredentialManager.create(context)

    private val googleIdOption: GetSignInWithGoogleOption =
        GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_CLIENT_ID)
            .build()

    private val credentialRequest = GetCredentialRequest(listOf(googleIdOption))

    fun startLogin(
        coroutineScope: CoroutineScope,
        onSuccessLogin: (String) -> Unit,
        onFailLogin: (String?) -> Unit,
    ) {
        coroutineScope.launch {
            runCatching {
                val result = credentialManager.getCredential(context, credentialRequest)
                handleSignIn(result, onSuccessLogin, onFailLogin)
            }
        }
    }

    private fun handleSignIn(
        result: GetCredentialResponse,
        onSuccessLogin: (String) -> Unit,
        onFailLogin: (String?) -> Unit,
    ) {
        val credential = result.credential
        if (credential.isCustomAndRightType()) {
            runCatching {
                GoogleIdTokenCredential.createFrom(credential.data)
            }.onSuccess {
                onSuccessLogin(it.idToken)
            }.onFailure {
                onFailLogin(it.toString())
            }
        } else {
            onFailLogin(null)
        }
    }

    private fun Credential.isCustomAndRightType() =
        this is CustomCredential && type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
}
