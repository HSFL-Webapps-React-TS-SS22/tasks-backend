package de.progeek.tasks.mapper

import org.bson.types.ObjectId
import org.mapstruct.Mapper

@Mapper
abstract class ObjectIdMapper {
    fun map(id: ObjectId): String = id.toHexString()
    fun map(id: String) = id.let { ObjectId(id) }
}
