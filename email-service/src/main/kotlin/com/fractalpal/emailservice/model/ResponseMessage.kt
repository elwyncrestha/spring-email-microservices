package com.fractalpal.emailservice.model

/**
 * In real this should handle more info about errors from providers
 */
data class ResponseMessage(val code: Int = CODE_SUCCESS, val message: String = MSG_SUCCESS) {
    companion object {
        const val MSG_SUCCESS = "email sent successfully"
        const val MSG_FAILED = "email sent failed"

        const val CODE_SUCCESS = 200
        const val CODE_ERROR = 400

        fun errorMessage() = ResponseMessage(CODE_ERROR, MSG_FAILED)
    }
}