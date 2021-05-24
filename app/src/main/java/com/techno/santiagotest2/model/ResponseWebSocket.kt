package com.techno.santiagotest2.model

data class ResponseWebSocket(
    val ref: Int? = null,
    val payload: Payload? = null,
    val topic: String? = null,
    val event: String? = null
)
