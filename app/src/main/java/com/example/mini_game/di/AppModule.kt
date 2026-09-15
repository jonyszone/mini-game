package com.example.mini_game.di

import android.content.Context
import androidx.room.Room
import com.example.mini_game.data.local.BrainSparkDatabase
import com.example.mini_game.data.local.QuizDao
import com.example.mini_game.data.remote.GeminiApi
import com.example.mini_game.data.repository.QuizRepositoryImpl
import com.example.mini_game.domain.repository.QuizRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindQuizRepository(impl: QuizRepositoryImpl): QuizRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BrainSparkDatabase =
        Room.databaseBuilder(context, BrainSparkDatabase::class.java, "brainspark.db").build()

    @Provides
    fun provideQuizDao(database: BrainSparkDatabase): QuizDao = database.quizDao()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Provides
    @Singleton
    fun provideGeminiApi(client: OkHttpClient): GeminiApi {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(GeminiApi::class.java)
    }
}
