package com.college.os.core.di

import android.content.Context
import androidx.room.Room
import com.college.os.core.data.CollegeDatabase
import com.college.os.feature.assignments.data.AssignmentDao
import com.college.os.feature.attendance.data.AttendanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CollegeDatabase {
        return Room.databaseBuilder(
            context,
            CollegeDatabase::class.java,
            "college_os_database"
        )
            // Important: This allows us to change database versions (1 -> 2)
            // without crashing. It recreates the DB if the schema changes.
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAttendanceDao(database: CollegeDatabase): AttendanceDao {
        return database.attendanceDao()
    }

    // New Provider for Assignments
    @Provides
    fun provideAssignmentDao(database: CollegeDatabase): AssignmentDao {
        return database.assignmentDao()
    }
}