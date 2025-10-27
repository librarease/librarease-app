package org.librarease.app.data.local.mapper

import org.librarease.app.data.local.entity.LibraryEntity
import org.librarease.app.domain.model.Library

fun Library.toEntity(): LibraryEntity {
    return LibraryEntity(
        id = id,
        name = name,
        phoneNo = phoneNo,
        email = email,
        logo = logo,
        createdAt = created_at,
        updatedAt = updated_at
    )
}

fun LibraryEntity.toDomain(): Library {
    return Library(
        id = id,
        name = name,
        phoneNo = phoneNo,
        email = email,
        logo = logo,
        created_at = createdAt,
        updated_at = updatedAt
    )
}
