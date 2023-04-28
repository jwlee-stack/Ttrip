package org.sfy.ttrip.data.remote.datasorce.base

interface DataToDomainMapper<T> {
    fun toDomainModel(): T
}