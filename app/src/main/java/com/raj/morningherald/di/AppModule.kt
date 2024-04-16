package com.raj.morningherald.di

import android.app.Application
import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.raj.morningherald.core.common.connectivity.ConnectivityChecker
import com.raj.morningherald.core.common.connectivity.ConnectivityCheckerImpl
import com.raj.morningherald.core.util.Constants.API_KEY
import com.raj.morningherald.core.util.Constants.BASE_URL
import com.raj.morningherald.core.common.dispatcher.DispatcherProviderImpl
import com.raj.morningherald.core.common.dispatcher.DispatcherProvider
import com.raj.morningherald.core.util.Constants.DB_NAME
import com.raj.morningherald.core.util.Constants.DEFAULT_PAGE_SIZE
import com.raj.morningherald.data.local.database.NewsDatabase
import com.raj.morningherald.data.local.entity.ArticleEntity
import com.raj.morningherald.data.remote.NewsApi
import com.raj.morningherald.data.remote.PagingArticle
import com.raj.morningherald.data.repository.NewsRepositoryImpl
import com.raj.morningherald.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() =
        OkHttpClient.Builder().addInterceptor(interceptor = Interceptor(API_KEY)).build()

    @Provides
    @Singleton
    fun provideNetwork(okHttpClient: OkHttpClient): NewsApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsApi,
        newsDatabase: NewsDatabase,
        connectivityChecker: ConnectivityChecker
    ): NewsRepository =
        NewsRepositoryImpl(newsApi, newsDatabase, connectivityChecker)

    @Provides
    @Singleton
    fun provideDataBase(app: Application, @DataBaseName dataBaseName: String): NewsDatabase {
        return Room.databaseBuilder(
            app,
            NewsDatabase::class.java,
            dataBaseName
        ).build()
    }

    @Provides
    @Singleton
    fun provideConnectivityChecker(@ApplicationContext context: Context): ConnectivityChecker =
        ConnectivityCheckerImpl(context)


    @Provides
    @Singleton
    fun provideDispatcher(): DispatcherProvider = DispatcherProviderImpl()


    @DataBaseName
    @Provides
    fun provideDataBaseName(): String = DB_NAME

}
