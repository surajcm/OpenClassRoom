package com.classroom.init.specs

data class SearchCriteria(
    val key: String,
    val value: Any,
    val operation: SearchOperation
)