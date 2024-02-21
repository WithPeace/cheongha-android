import androidx.multidex.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

object GoogleLogin {

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(BuildConfig.CLIENT_ID)
    .build()
}