package com.sampleNote.work.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sampleNote.work.BuildConfig
import com.sampleNote.work.model.Priority
import com.sampleNote.work.model.repository.network.NoteService
import com.sampleNote.work.util.DateTimeDeserializer
import com.sampleNote.work.util.DateTimeSerializer
import com.sampleNote.work.util.PriorityDeserializer
import com.sampleNote.work.util.PrioritySerializer
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.LocalDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val baseUrl: String) {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor().setLevel(
                if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
        )
    }

    @Singleton
    @Provides
    fun provideGson(): Gson{
        return GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, DateTimeSerializer())
                .registerTypeAdapter(LocalDateTime::class.java, DateTimeDeserializer())
                .registerTypeAdapter(Priority::class.java, PrioritySerializer())
                .registerTypeAdapter(Priority::class.java, PriorityDeserializer())
                .create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient{
        return OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit{
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Singleton
    @Provides
    fun provideNotesApiService(retrofit: Retrofit): NoteService{
        return retrofit.create(NoteService::class.java)
    }
}