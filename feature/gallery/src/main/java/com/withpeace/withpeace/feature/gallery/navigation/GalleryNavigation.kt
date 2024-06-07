package com.withpeace.withpeace.feature.gallery.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.withpeace.withpeace.feature.gallery.GalleryRoute

const val GALLERY_ROUTE = "gallery_route"
const val GALLERY_IMAGE_LIMIT_ARGUMENT = "image_limit"
const val GALLERY_ALREADY_IMAGE_COUNT_ARGUMENT = "image_count"
const val GALLERY_ROUTE_WITH_ARGUMENT =
    "$GALLERY_ROUTE/{$GALLERY_IMAGE_LIMIT_ARGUMENT}/{$GALLERY_ALREADY_IMAGE_COUNT_ARGUMENT}"

fun NavController.navigateToGallery(
    navOptions: NavOptions? = null,
    imageLimit: Int = 5,
    currentImageCount: Int = 0,
) =
    navigate("$GALLERY_ROUTE/$imageLimit/$currentImageCount", navOptions)

fun NavGraphBuilder.galleryNavGraph(
    onClickBackButton: () -> Unit,
    onCompleteRegisterImages: (List<String>) -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    composable(
        route = GALLERY_ROUTE_WITH_ARGUMENT,
        arguments = listOf(
            navArgument(GALLERY_IMAGE_LIMIT_ARGUMENT) { type = NavType.IntType },
            navArgument(GALLERY_ALREADY_IMAGE_COUNT_ARGUMENT) { type = NavType.IntType },
        ),
    ) {
        GalleryRoute(
            onClickBackButton = onClickBackButton,
            onCompleteRegisterImages = onCompleteRegisterImages,
            onShowSnackBar = onShowSnackBar
        )
    }
}
