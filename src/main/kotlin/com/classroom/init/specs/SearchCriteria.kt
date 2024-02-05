package com.classroom.init.specs

import java.io.Serializable

class SearchCriteria: Serializable {

    val serialVersionUID: Long = 4328743
    private var key: String? = null

    @Transient
    private var value: Any? = null
    private var operation: SearchOperation? = null

    constructor(key: String, value: String?, operation: SearchOperation)

    fun getKey(): String? {
        return key
    }

    fun setKey(key: String?) {
        this.key = key
    }

    fun getValue(): Any? {
        return value
    }

    fun setValue(value: Any?) {
        this.value = value
    }

    fun getOperation(): SearchOperation? {
        return operation
    }

    fun setOperation(operation: SearchOperation?) {
        this.operation = operation
    }
}