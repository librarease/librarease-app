package org.librarease.app.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.librarease.app.data.local.dao.BookDao
import org.librarease.app.data.local.dao.BookDetailDao
import org.librarease.app.data.remote.LibrareaseApi
import org.librarease.app.data.remote.service.LibrareaseNetworkService
import org.librarease.app.data.remote.service.LibrareaseNetworkServiceImpl
import org.librarease.app.data.repository.AuthRepositoryImpl
import org.librarease.app.data.repository.LibrareaseRepoImpl
import org.librarease.app.domain.repository.AuthRepository
import org.librarease.app.domain.repository.LibrareaseRepository
import org.librarease.app.di.module.AuthInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseTokenProvider(): FirebaseTokenProvider = FirebaseTokenProvider()

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenProvider: FirebaseTokenProvider): AuthInterceptor =
        AuthInterceptor(tokenProvider)

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideLibrareaseApi(okHttpClient: OkHttpClient): LibrareaseApi {
        val gson = GsonBuilder().create()
        return Retrofit.Builder()
            .baseUrl(LibrareaseApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LibrareaseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLibrareaseNetworkService(
        api: LibrareaseApi,
        tokenProvider: FirebaseTokenProvider
    ): LibrareaseNetworkService = LibrareaseNetworkServiceImpl(api, tokenProvider)

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        networkService: LibrareaseNetworkService
    ): AuthRepository = AuthRepositoryImpl(auth, networkService)

    @Provides
    @Singleton
    fun provideLibrareaseRepository(
        networkService: LibrareaseNetworkService,
        bookDao: BookDao,
        bookDetailDao: BookDetailDao
    ): LibrareaseRepository = LibrareaseRepoImpl(networkService, bookDao, bookDetailDao)


}