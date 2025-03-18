package com.jackson.networkapp.network.di

import com.jackson.networkapp.network.NetworkConfig
import com.jackson.networkapp.network.api.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 网络模块类
 * 负责创建和配置OkHttp和Retrofit实例
 */
object NetworkModule {
    
    // 连接超时时间（秒）
    private const val CONNECT_TIMEOUT = 30L
    
    // 读取超时时间（秒）
    private const val READ_TIMEOUT = 30L
    
    // 写入超时时间（秒）
    private const val WRITE_TIMEOUT = 30L
    
    /**
     * 创建Gson实例
     */
    private fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
    
    /**
     * 创建OkHttpClient实例
     */
    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (NetworkConfig.isDebug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
    
    /**
     * 创建Retrofit实例
     */
    private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConfig.getBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    /**
     * 提供API服务实例
     */
    val apiService: ApiService by lazy {
        provideRetrofit(provideOkHttpClient(), provideGson()).create(ApiService::class.java)
    }
} 