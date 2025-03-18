package com.jackson.networkapp.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jackson.networkapp.network.api.Repository
import com.jackson.networkapp.network.api.Result
import com.jackson.networkapp.network.api.safeApiCall
import com.jackson.networkapp.network.model.ApiResponse
import kotlinx.coroutines.launch

/**
 * 主ViewModel类
 * 负责处理UI相关的数据流和业务逻辑
 */
class MainViewModel : ViewModel() {
    
    private val repository = Repository.getInstance()
    
    private val _dataResult = MutableLiveData<Result<ApiResponse>>()
    val dataResult: LiveData<Result<ApiResponse>> = _dataResult
    
    /**
     * 加载数据
     * @param page 页码
     */
    fun loadData(page: Int = 1) {
        viewModelScope.launch {
            _dataResult.value = Result.Loading
            val result = safeApiCall { repository.getData(page) }
            _dataResult.value = result
        }
    }
} 