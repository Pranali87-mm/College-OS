package com.college.os.feature.planner.domain

import com.college.os.feature.assignments.data.AssignmentEntity
import com.college.os.feature.timetable.data.TimetableEntity

/**
 * A sealed class allowing us to display different types of items
 * in the same list (Recycler/LazyColumn).
 */
sealed class PlannerItem {

    // A specific class session (e.g., "Physics - 10:00 AM")
    data class ClassSession(
        val entity: TimetableEntity
    ) : PlannerItem()

    // An assignment due on this day
    data class AssignmentDue(
        val entity: AssignmentEntity
    ) : PlannerItem()

    // Future: data class ExamEvent(...)
    // Future: data class PersonalEvent(...)
}