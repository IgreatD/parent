package com.sjkj.parent.api

import com.google.gson.GsonBuilder
import com.sjkj.parent.BuildConfig
import com.sjkj.parent.common.Common
import com.sjkj.parent.utils.logD
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * @author by dingl on 2017/9/16.
 * @desc RetrofitInit
 */
class RetrofitInit {

    init {
        initGson()
        initHttpLog()
        initOkhttp()
        initRetrofit()
    }

    private fun initHttpLog(): HttpLoggingInterceptor {
        val logInterceptor = HttpLoggingInterceptor { message ->
            logD(this::class.java, message)
        }
        if (BuildConfig.DEBUG)
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            logInterceptor.level = HttpLoggingInterceptor.Level.NONE
        return logInterceptor

    }

    private fun initRetrofit(): Retrofit {
        val nullOnEmptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }
        return Retrofit.Builder()
                .client(initOkhttp())
                .baseUrl(ParentUri.BASE_URL)
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create(initGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    private fun initOkhttp(): OkHttpClient {
        return OkHttpClient
                .Builder()
                .connectTimeout(Common.RECONNECT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
//                .addInterceptor(initHttpLog())
                .build()
    }

    private fun initGson() = GsonBuilder().create()

    fun create(): ParentApi = initRetrofit().create(ParentApi::class.java)

}
