package com.college.os.feature.timetable.di

import com.college.os.feature.timetable.data.TimetableRepositoryImpl
import com.college.os.feature.timetable.domain.TimetableRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimetableModule {

    @Binds
    @Singleton
    abstract fun bindTimetableRepository(
        impl: TimetableRepositoryImpl
    ): TimetableRepository
}