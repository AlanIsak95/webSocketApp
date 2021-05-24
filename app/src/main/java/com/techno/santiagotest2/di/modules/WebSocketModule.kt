package com.techno.santiagotest2.di.modules

import com.google.gson.JsonObject
import com.techno.santiagotest2.utils.Constans
import com.techno.santiagotest2.utils.Constans.Companion.END_POINT
import com.techno.santiagotest2.utils.Constans.Companion.JSON_CHANNEL
import com.techno.santiagotest2.utils.Constans.Companion.JSON_SEND_DATA
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object WebSocketModule {

    @Provides
    @Singleton
    fun getInterceptor() : Interceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    @Provides
    @Singleton
    fun getOkHttpClient(interceptor: Interceptor):OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()


    @Provides
    @Singleton
    fun getRequestForClient():Request =
        Request.Builder().url(END_POINT).build()

    @Provides
    @Singleton
    @Named(JSON_CHANNEL)
    fun getJsonChannel():JSONObject =
        JSONObject().apply {
            put("topic", "dsa:test")
            put("event", "phx_join")
            put("ref", 1)
            put("payload", JSONObject())
        }

    @Provides
    @Singleton
    @Named(JSON_SEND_DATA)
    fun getJsonSendData():JSONObject =
        JSONObject().apply {
            put("topic", "dsa:test")
            put("event", "send")
            put("ref", 12)
        }



}