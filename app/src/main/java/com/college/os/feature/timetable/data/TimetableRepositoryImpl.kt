package com.college.os.feature.timetable.data

import com.college.os.feature.timetable.domain.TimetableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimetableRepositoryImpl @Inject constructor(
    private val dao: TimetableDao
) : TimetableRepository {

    override fun getClassesForDay(day: String): Flow<List<TimetableEntity>> {
        return dao.getClassesForDay(day)
    }

    override fun getAllClasses(): Flow<List<TimetableEntity>> {
        return dao.getAllClasses()
    }

    override suspend fun insertClass(timetableEntry: TimetableEntity) {
        dao.insertClass(timetableEntry)
    }

    override suspend fun deleteClass(timetableEntry: TimetableEntity) {
        dao.deleteClass(timetableEntry)
    }
}