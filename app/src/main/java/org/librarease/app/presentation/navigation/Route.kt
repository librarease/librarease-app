package org.librarease.app.presentation.navigation

sealed class Route(val route: String) {
    data object SignIn : Route("sign_in")
    data object ForgotPassword : Route("forgot_password")
    data object SignUp : Route("sign_up")
    data object VerifyEmail : Route("verify_email")
    data object Profile : Route("profile")
    data object Main : Route("main")
    data object AllBooks : Route("all_books")

    override fun toString(): String = route
}