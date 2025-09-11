package org.librarease.app.data.mapper

import org.librarease.app.data.remote.response.BookListResponse
import org.librarease.app.data.remote.response.LibraryListResponse
import org.librarease.app.data.remote.response.MembershipListResponse
import org.librarease.app.data.remote.response.SubscriptionListResponse
import org.librarease.app.data.remote.response.SubscriptionResponse
import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library
import org.librarease.app.domain.model.Membership
import org.librarease.app.domain.model.Subscription

object ResponseMapper {

    val bookListMapper: (BookListResponse) -> List<BookItem> = { response ->
        val responseData = response.bookList
        val bookList = ArrayList<BookItem>()
        
        responseData?.forEach { book ->
            book?.let {
                if(it.title != null) {
                    bookList.add(
                        BookItem(
                            title = it.title,
                            author = it.author ?: "",
                            cover = it.cover ?: ""
                        )
                    )
                }
            }
        }
        bookList
    }
    
    val libraryListMapper: (LibraryListResponse) -> List<Library> = { response ->
        val responseData = response.libraryList
        val libraryList = ArrayList<Library>()
        
        responseData?.forEach { library ->
            library?.let {
                if (it.id != null && it.name != null) {
                    libraryList.add(
                        Library(
                            id = it.id,
                            name = it.name,
                            phoneNo = it.phoneNo,
                            email = it.email,
                            logo = it.logo
                        )
                    )
                }
            }
        }
        
        libraryList
    }
    
    val libraryMapper: (LibraryListResponse.Library) -> Library = { library ->
        Library(
            id = library.id ?: "",
            name = library.name,
            phoneNo = library.phoneNo,
            email = library.email,
            logo = library.logo
        )
    }
    
    val membershipListMapper: (MembershipListResponse) -> List<Membership> = { response ->
        val responseData = response.membershipList
        val membershipList = ArrayList<Membership>()
        
        responseData?.forEach { membership ->
            membership?.let {
                if (it.id != null && it.name != null && it.libraryId != null) {
                    membershipList.add(
                        Membership(
                            id = it.id,
                            name = it.name,
                            description = it.description ?: "",
                            price = it.price ?: 0.0,
                            durationDays = it.durationDays ?: 30,
                            loanPeriod = it.loadPeriod ?: 7,
                            libraryId = it.libraryId,
                            maxBooks = it.maxBooks ?: 5,
                        )
                    )
                }
            }
        }
        
        membershipList
    }

    val subscriptionListMapper: (SubscriptionListResponse) -> List<Subscription> = { responseList ->
        val subscriptionList = ArrayList<Subscription>()
        
        responseList.subscriptions?.forEach { subscription ->
            subscriptionList.add(
                Subscription(
                    id = subscription.id.orEmpty(),
                    userId = subscription.userId.orEmpty(),
                    userName = subscription.user?.name.orEmpty(),
                    membershipId = subscription.membershipId.orEmpty(),
                    membershipName = subscription.membership?.name.orEmpty(),
                    libraryId = subscription.membership?.library?.id.orEmpty(),
                    libraryName = subscription.membership?.library?.name.orEmpty(),
                    createdDate = subscription.createdDate.orEmpty(),
                    expireDate = subscription.expireDate.orEmpty(),
                    amount = subscription.amount ?: 0,
                    finePerDay = subscription.finePerDay ?: 0,
                    loanPeriod = subscription.loanPeriod ?: 0,
                    activeLoanLimit = subscription.activeLoanLimit ?: 0
                )
            )

        }
        
        subscriptionList
    }
}