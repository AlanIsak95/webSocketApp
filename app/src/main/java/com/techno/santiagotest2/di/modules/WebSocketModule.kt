package com.techno.santiagotest2.di.modules

import com.techno.santiagotest2.utils.Constans.Companion.END_POINT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
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



}