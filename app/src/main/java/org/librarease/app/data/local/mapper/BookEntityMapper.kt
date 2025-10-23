package org.librarease.app.data.local.mapper

import org.librarease.app.data.local.entity.BookDetailEntity
import org.librarease.app.data.local.entity.BookEntity
import org.librarease.app.domain.model.BookDetail
import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.BorrowingInfo
import org.librarease.app.domain.model.Library

/**
 * Mapper functions to convert between domain models and Room entities
 */

// BookItem <-> BookEntity
fun BookItem.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        author = author,
        cover = cover,
        code = code,
        year = year,
        libraryId = libraryId,
        libraryName = libraryName
    )
}

fun BookEntity.toDomain(): BookItem {
    return BookItem(
        id = id,
        title = title,
        author = author,
        cover = cover,
        code = code,
        year = year,
        libraryId = libraryId,
        libraryName = libraryName
    )
}

fun BookDetail.toEntity(): BookDetailEntity {
    return BookDetailEntity(
        id = id,
        title = title,
        author = author,
        cover = cover,
        code = code,
        year = year,
        libraryId = library.id,
        libraryName = library.name,
        libraryPhoneNo = library.phoneNo,
        libraryEmail = library.email,
        libraryLogo = library.logo,
        borrowCount = borrowCount,
        currentBorrowingId = currentBorrowing?.id,
        currentBorrowingBorrowedAt = currentBorrowing?.borrowedAt,
        currentBorrowingDueAt = currentBorrowing?.dueAt,
        currentBorrowingReturnedAt = currentBorrowing?.returnedAt,
        currentBorrowingIsReturned = currentBorrowing?.isReturned,
        currentBorrowingFine = currentBorrowing?.fine
    )
}

fun BookDetailEntity.toDomain(): BookDetail {
    return BookDetail(
        id = id,
        title = title,
        author = author,
        cover = cover,
        code = code,
        year = year,
        library = Library(
            id = libraryId,
            name = libraryName ?: "",
            phoneNo = libraryPhoneNo,
            email = libraryEmail,
            logo = libraryLogo,
            created_at = null,
            updated_at = null
        ),
        borrowCount = borrowCount,
        currentBorrowing = if (currentBorrowingId != null) {
            BorrowingInfo(
                id = currentBorrowingId,
                borrowedAt = currentBorrowingBorrowedAt ?: "",
                dueAt = currentBorrowingDueAt ?: "",
                isReturned = currentBorrowingIsReturned ?: false,
                returnedAt = currentBorrowingReturnedAt,
                fine = currentBorrowingFine
            )
        } else null
    )
}
