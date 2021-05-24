package com.techno.santiagotest2.ui


import com.techno.santiagotest2.interfaces.IOnMessageReceiver
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class SocketListener(callback_ : IOnMessageReceiver): WebSocketListener() {

        private val callback = callback_


        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            callback.success("Connected")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            callback.success(text)
        }


        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            callback.failure("Channel Disconnected")
        }
    }