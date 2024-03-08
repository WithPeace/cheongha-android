package com.withpeace.withpeace.feature.registerpost

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun RegisterPostRoute(
    viewModel: RegisterPostViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
) {

}

@Preview(showBackground = true)
@Composable
fun RegisterPostRoutePreview() {
    WithpeaceTheme {

    }
}
