package com.college.os.core.di

import android.content.Context
import androidx.room.Room
import com.college.os.core.data.CollegeDatabase
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

    // 1. Provide the Database Instance
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
            // .fallbackToDestructiveMigration() // Uncomment this during dev if you change schema often
            .build()
    }

    // 2. Provide the Attendance Dao
    // Now, any Repository that needs 'AttendanceDao' just asks for it,
    // and Hilt extracts it from the database automatically.
    @Provides
    fun provideAttendanceDao(database: CollegeDatabase): AttendanceDao {
        return database.attendanceDao()
    }
}