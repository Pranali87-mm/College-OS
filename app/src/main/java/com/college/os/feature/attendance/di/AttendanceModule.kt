package com.college.os.feature.attendance.di

import com.college.os.feature.attendance.data.AttendanceRepositoryImpl
import com.college.os.feature.attendance.domain.AttendanceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AttendanceModule {

    /**
     * @Binds tells Hilt:
     * "Whenever someone asks for 'AttendanceRepository', give them 'AttendanceRepositoryImpl'."
     */
    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(
        impl: AttendanceRepositoryImpl
    ): AttendanceRepository
}