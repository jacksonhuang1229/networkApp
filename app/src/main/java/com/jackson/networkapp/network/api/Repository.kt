package com.jackson.networkapp.network.api

import com.jackson.networkapp.network.di.NetworkModule
import com.jackson.networkapp.network.model.ApiResponse
import retrofit2.Response

/**
 * 数据仓库类
 * 负责处理所有的网络请求，对外提供数据访问接口
 */
class Repository {
    
    private val apiService = NetworkModule.apiService
    
    /**
     * 获取数据
     * @param page 页码
     * @return API响应
     */
    suspend fun getData(page: Int): Response<ApiResponse> {
        return apiService.getData(page)
    }
    
    companion object {
        // 单例模式
        @Volatile
        private var instance: Repository? = null
        
        fun getInstance(): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository().also { instance = it }
            }
        }
    }
} 