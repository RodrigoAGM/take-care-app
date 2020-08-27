package com.example.takecare.data.api

import java.lang.Exception


/**
 * Created by Enzo Lizama Paredes on 8/26/20.
 * Contact: lizama.enzo@gmail.com
 */


sealed class OperationResult<out T> {
    data class Success<T>(val data: T?): OperationResult<T>()
    data class Error(val exception: Exception?): OperationResult<Nothing>()
}