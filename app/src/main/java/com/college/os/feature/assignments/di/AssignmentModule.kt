package com.college.os.feature.assignments.di

import com.college.os.feature.assignments.data.AssignmentRepositoryImpl
import com.college.os.feature.assignments.domain.AssignmentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AssignmentModule {

    @Binds
    @Singleton
    abstract fun bindAssignmentRepository(
        impl: AssignmentRepositoryImpl
    ): AssignmentRepository
}