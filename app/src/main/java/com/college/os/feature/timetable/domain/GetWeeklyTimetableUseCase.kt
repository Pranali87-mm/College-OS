package com.college.os.feature.timetable.domain

import com.college.os.feature.timetable.data.TimetableEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeeklyTimetableUseCase @Inject constructor(
    private val repository: TimetableRepository
) {
    operator fun invoke(): Flow<List<TimetableEntity>> {
        return repository.getAllClasses()
    }
}