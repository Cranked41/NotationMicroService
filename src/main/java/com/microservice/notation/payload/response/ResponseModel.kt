package com.microservice.notation.payload.response

data class ResponseModel<T>(
        val success: Boolean,
        val code: Int,
        val message: String?,
        val data: T?
) {
    companion object {
        fun <T> success(code: Int, data: T): ResponseModel<T> {
            return ResponseModel(success = true, code = code, message = null, data = data)
        }

        fun <T> error(code: Int, message: String): ResponseModel<T> {
            return ResponseModel(success = false, code = code, message = message, data = null)
        }
    }
}