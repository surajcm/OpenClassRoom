package com.classroom.init.specs

data class SearchCriteria(
    var key: String? = null,
    var value: Any? = null,
    var operation: SearchOperation? = null
)