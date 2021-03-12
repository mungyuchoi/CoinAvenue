package com.moon.coinavenue.network.repository

import android.util.Log
import com.moon.coinavenue.network.sealed.Output
import retrofit2.Response
import java.io.IOException

open class BaseRepository {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, error: String): T? {
        val result = baseCall(call, error)
        var output: T? = null
        when (result) {
            is Output.Success -> output = result.output
            is Output.Error -> Log.e("Error", "The $error and the ${result.exception}")
        }
        return output
    }


    suspend fun <T : Any> baseCall(
        call: suspend () -> Response<T>,
        error: String
    ): Output<T>? {
        val response = call.invoke()
        return if (response.isSuccessful)
            Output.Success(response.body()!!)
        else
            Output.Error(IOException("OOps.. Something went wrong due to $error"))
    }
}