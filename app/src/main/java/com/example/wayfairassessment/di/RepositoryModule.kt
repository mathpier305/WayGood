package com.example.wayfairassessment.di

import com.example.wayfairassessment.repository.WFRepository
import com.example.wayfairassessment.repository.WFRepositoryImpl
import com.example.wayfairassessment.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesWFRepository(apiService: ApiService) : WFRepository {
        return WFRepositoryImpl(apiService)
    }
}