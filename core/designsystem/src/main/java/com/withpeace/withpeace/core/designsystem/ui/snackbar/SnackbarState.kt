package com.withpeace.withpeace.core.designsystem.ui.snackbar

data class SnackbarState(
    val message: String,
    val snackbarType: SnackbarType = SnackbarType.Normal,
)

sealed interface SnackbarType {
    data object Normal : SnackbarType
    data class Navigator(val actionName: String, val action: () -> Unit) :
        SnackbarType
}
