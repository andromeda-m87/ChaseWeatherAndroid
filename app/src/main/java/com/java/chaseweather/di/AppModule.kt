package com.java.chaseweather.di

import android.app.Application
import android.content.Context
import com.java.chaseweather.MainActivity
import com.java.chaseweather.location.LocationRepository
import com.java.chaseweather.location.impl.LocationRepositoryImpl
import com.java.chaseweather.networking.remote.ApiService
import com.java.chaseweather.networking.remote.DataRepository
import com.java.chaseweather.networking.remote.NetworkRepository
import com.java.chaseweather.utils.Constants.BASE_URL
import com.java.chaseweather.viewmodel.SearchViewModelFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    fun providesApplication(): Application = app

    @Provides
    fun providesContext(): Context = app
}

@Module
class ViewModelFactoryModule {

    @Provides
    @Singleton
    fun provideSearchViewModelFactory(locationRepo: LocationRepository, networkRepository: NetworkRepository) =
        SearchViewModelFactory(locationRepo, networkRepository)
}

@Module
class LocationModule {

    @Provides
    @Singleton
    fun provideLocationRepository(context: Context): LocationRepository =
        LocationRepositoryImpl(context)
}

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        converterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideNetworkRepo(weatherApi: ApiService): NetworkRepository = DataRepository(weatherApi)
}

@Singleton
@Component(
    modules = [
        AppModule::class,
        LocationModule::class,
        ViewModelFactoryModule::class,
        NetworkModule::class
    ]
)

interface AppComponent{
    fun inject(mainActivity: MainActivity)
}