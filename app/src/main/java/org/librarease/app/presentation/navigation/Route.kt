package org.librarease.app.presentation.navigation

import android.net.Uri

sealed class Route(val route: String) {
    data object SignIn : Route("sign_in")
    data object ForgotPassword : Route("forgot_password")
    data object SignUp : Route("sign_up")
    data object VerifyEmail : Route("verify_email")
    data object Profile : Route("profile")
    data object Main : Route("main")
    data object AllBooks : Route("all_books")
    data object LibraryDetail : Route("library_detail")

    data object Books: Route("books")

    data object Libraries: Route("libraries")

    data object Subscriptions: Route("subscriptions")

    data object Borrowings: Route("borrowings")

    data object SubscriptionQR: Route("subscription_qr/{subscriptionId}") {
        fun createRoute(subscriptionId: String) = "subscription_qr/${Uri.encode(subscriptionId)}"
    }
    
    class LibraryDetailWithId(libraryId: String) : Route("library_detail/$libraryId")

    override fun toString(): String = route
}