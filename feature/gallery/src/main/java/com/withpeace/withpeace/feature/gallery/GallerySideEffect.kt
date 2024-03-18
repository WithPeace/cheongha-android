package com.withpeace.withpeace.feature.gallery

sealed interface GallerySideEffect {
    data object SelectImageFail : GallerySideEffect
}
