package com.fractalpal.emailclient.model

data class ResponseMessage(val code: Int = CODE_SUCCESS, val message: String = MSG_SUCCESS) {
    companion object {
        const val MSG_SUCCESS = "email sent successfully"
        const val MSG_FALLBACK= "email services not available now but your message will be sent asap after recovery"

        const val CODE_SUCCESS = 200
        const val CODE_MESSAGE_IN_QUEUE = 500

    }
}
