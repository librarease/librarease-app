package org.librarease.app.presentation.navigation

sealed class Route(val route: String) {
    data object SignIn : Route("sign_in")
    data object ForgotPassword : Route("forgot_password")
    data object SignUp : Route("sign_up")
    data object VerifyEmail : Route("verify_email")
    data object Profile : Route("profile")
    data object Main : Route("main")
    data object AllBooks : Route("all_books")
    data object LibraryDetail : Route("library_detail")
    
    // Custom constructor for library detail with library ID only
    class LibraryDetailWithId(libraryId: String) : Route("library_detail/$libraryId")

    override fun toString(): String = route
}