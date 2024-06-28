package com.withpeace.withpeace.feature.gallery

sealed interface GallerySideEffect {
    data object SelectImageNoMore : GallerySideEffect
    data object SelectImageNoApplyType : GallerySideEffect
    data object SelectImageOverSize : GallerySideEffect
}
