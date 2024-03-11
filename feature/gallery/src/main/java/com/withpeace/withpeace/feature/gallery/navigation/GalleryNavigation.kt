package com.withpeace.withpeace.feature.gallery.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.gallery.GalleryRoute

const val GALLERY_ROUTE = "gallery_route"

fun NavController.navigateToGallery(navOptions: NavOptions? = null) =
    navigate(GALLERY_ROUTE, navOptions)

fun NavGraphBuilder.galleryNavGraph(
    onClickBackButton: () -> Unit,
    onCompleteRegisterImages: (List<String>) -> Unit,
) {
    composable(route = GALLERY_ROUTE) {
        GalleryRoute(
            onClickBackButton = onClickBackButton,
            onCompleteRegisterImages = onCompleteRegisterImages,
        )
    }
}
