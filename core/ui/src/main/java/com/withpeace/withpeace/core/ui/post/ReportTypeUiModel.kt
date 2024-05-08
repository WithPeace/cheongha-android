package com.withpeace.withpeace.core.ui.post

import com.withpeace.withpeace.core.domain.model.post.ReportType

enum class ReportTypeUiModel(val postTitle: String, val commentTitle: String) {
    DUPLICATE(
        "중복 / 도배성 게시글이에요",
        "중복 / 도배성 댓글이에요",
    ),
    ADVERTISEMENT(
        "광고 / 홍보 글이에요",
        "광고 / 홍보 댓글이에요",
    ),
    INAPPROPRIATE(
        "게시판 성격에 부적절한 글이에요",
        "게시판 성격에 부적절한 댓글이에요",
    ),
    PROFANITY(
        "욕설 / 혐오 표현이 사용된 글이에요",
        "욕설 / 혐오 표현이 사용된 댓글이에요",
    ),
    OBSCENITY(
        "음란성 / 선정적인 글이에요",
        "음란성 / 선정적인 댓글이에요",
    )
}

fun ReportTypeUiModel.toDomain(): ReportType {
    return when (this) {
        ReportTypeUiModel.DUPLICATE -> ReportType.DUPLICATE
        ReportTypeUiModel.ADVERTISEMENT -> ReportType.ADVERTISEMENT
        ReportTypeUiModel.INAPPROPRIATE -> ReportType.INAPPROPRIATE
        ReportTypeUiModel.PROFANITY -> ReportType.PROFANITY
        ReportTypeUiModel.OBSCENITY -> ReportType.OBSCENITY
    }
}
