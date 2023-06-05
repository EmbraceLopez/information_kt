package com.yeyu.information.di

import com.yeyu.information.net.InformationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideInformationService(): InformationService {
        return InformationService.create()
    }

}