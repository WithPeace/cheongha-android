package com.withpeace.withpeace.core.domain.model.error

enum class ResponseError(val serverErrorCode: Int? = null) : CheonghaError {
    /* Server 400 */
    NOT_FOUND_RESOURCE(40000), // NOT_END_POINT 으로 쓰일 수도 있음
    INVALID_ARGUMENT(40001),
    INVALID_PROVIDER(40002),
    METHOD_NOT_ALLOWED(40003),
    UNSUPPORTED_MEDIA_TYPE(40004),
    MISSING_REQUEST_PARAMETER(40005),
    METHOD_ARGUMENT_TYPE_MISMATCH(40006),
    DUPLICATE_RESOURCE(40007),

    /* Server 401 */
    EXPIRED_TOKEN_ERROR(40100),
    INVALID_TOKEN_ERROR(40101), // Refresh Token 만료시
    TOKEN_MALFORMED_ERROR(40102),
    TOKEN_TYPE_ERROR(40103),
    TOKEN_UNSUPPORTED_ERROR(40104),
    TOKEN_GENERATION_ERROR(40105),
    FAILURE_LOGIN(40106),
    FAILURE_LOGOUT(40107),
    TOKEN_UNKNOWN_ERROR(40106),

    /* Server 403 */
    ACCESS_DENIED_ERROR(40300),

    /* Server 404 */
    NOT_FOUND_USER(40401),
    NOT_FOUND_POST(40402),

    /* Server 500 */
    SERVER_ERROR(50000),
    AUTH_SERVER_USER_INFO_ERROR(50001),

    /* UnKnown Error */
    UNKNOWN_ERROR,

    /* Network Fail */
    HTTP_EXCEPTION_ERROR;

    companion object {
        fun findByCode(code: Int): ResponseError {
            return entries.find {
                it.serverErrorCode == code
            } ?: UNKNOWN_ERROR
        }
    }
}

