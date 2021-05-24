package com.techno.santiagotest2.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.techno.santiagotest2.R
import com.techno.santiagotest2.model.Response
import com.techno.santiagotest2.model.ResponseWebSocket
import com.techno.santiagotest2.interfaces.IOnMessageReceiver
import com.techno.santiagotest2.utils.Constans.Companion.CHANNEL_TEXT
import com.techno.santiagotest2.utils.Constans.Companion.CONNECTED
import com.techno.santiagotest2.utils.Constans.Companion.JSON_CHANNEL
import com.techno.santiagotest2.utils.Constans.Companion.JSON_SEND_DATA
import com.techno.santiagotest2.utils.Constans.Companion.WEBSOCKET_OFF_TEXT
import com.techno.santiagotest2.utils.Constans.Companion.WEBSOCKET_TEXT
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IOnMessageReceiver {


    @Inject
    lateinit var client: OkHttpClient
    @Inject
    lateinit var request: Request

    @Inject
    @Named(JSON_CHANNEL)
    lateinit var jsonChannel :JSONObject

    @Inject
    @Named(JSON_SEND_DATA)
    lateinit var jsonSendData :JSONObject

    lateinit var webSocket : WebSocket
    private lateinit var socketListener: SocketListener
    private lateinit var btn : Button
    private lateinit var textConnectionStatus : TextView
    private lateinit var textChannelStatus : TextView
    private lateinit var responseValue : TextView
    private lateinit var detailProgress : TextView
    private lateinit var textToSend : EditText
    private lateinit var progressBar : ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initSocketListener()
        initListeners()
        showProgress(true)

    }

    private fun initView() {

        btn                 = findViewById(R.id.sendDataBtn)
        textConnectionStatus= findViewById(R.id.textConnectionStatus)
        textToSend          = findViewById(R.id.textToSend)
        textChannelStatus   = findViewById(R.id.textChannelStatus)
        responseValue       = findViewById(R.id.responseValue)
        progressBar         = findViewById(R.id.progressBar)
        detailProgress      = findViewById(R.id.detailProgress)

    }


    private fun initSocketListener() {

        socketListener = SocketListener(this)
        webSocket = client.newWebSocket(request, socketListener)

        connectToChannel()

    }

    private fun connectToChannel() {

        Handler().postDelayed(
            {
                webSocket.send(jsonChannel.toString())
            },6000 // 6 seg
        )

    }



    private fun initListeners() {


        btn.setOnClickListener { sendData() }

        // Edit text enter key listener
        textToSend.setOnKeyListener(object : OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    hideSoftKeyboard()
                    sendData()
                    textToSend.clearFocus()

                    return true
                }
                return false
            }
        })


    }

    private fun sendData() {

        jsonSendData.apply {
            put("payload", JSONObject().apply { put("value", textToSend.text.toString()) })
        }

        webSocket.send(jsonSendData.toString())

    }

    private fun showProgress(boolean: Boolean){

        if (boolean){

            progressBar.visibility = VISIBLE
            textToSend.isEnabled = false
            btn.isEnabled = false
            detailProgress.visibility = VISIBLE

        }else{
            progressBar.visibility = GONE
            detailProgress.visibility = GONE
            textToSend.isEnabled = true
            btn.isEnabled = true

        }

    }

    override fun success(string: String) {


        if (string == CONNECTED){

            textConnectionStatus.text = WEBSOCKET_TEXT

        }else{


            val responseWebSocket = Gson().fromJson(string, ResponseWebSocket::class.java)
            var payloadResponse : Response?=null

            responseWebSocket.payload?.response?.let {

                payloadResponse = it

            }?: run {payloadResponse = null }


            when(responseWebSocket.ref){
                1 ->
                    runOnUiThread{
                        showProgress(false)
                        textChannelStatus.text = CHANNEL_TEXT
                    }
                else ->
                    runOnUiThread{
                    responseValue.text = payloadResponse?.value.toString()
                }
            }



        }




    }

    override fun failure(string: String) {

        runOnUiThread{

            showProgress(false)
            textConnectionStatus.text = WEBSOCKET_OFF_TEXT
            textChannelStatus.text = string

        }


   }


    fun Activity.hideSoftKeyboard(){
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

}