package com.boostcamp.and03.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.boostcamp.and03.ui.screen.booklist.model.BookUiModel

@Stable
class MainNavigator(
    val navController: NavHostController
) {
    val startDestination = Route.Booklist

    fun navigateToBooklist() {
        navController.navigate(Route.Booklist)
    }

    fun navigateToBookSearch() {
        navController.navigate(Route.BookSearch)
    }

    fun navigateToAddBook() {
        navController.navigate(Route.AddBook)
    }

    fun navigateToMyPage() {
        navController.navigate(Route.MyPage)
    }

    fun navigateToBookDetail(book: BookUiModel) {
        navController.navigate(
            Route.BookDetail(bookId = book.id)
        )
    }

    fun navigateToCanvas(memoId: String) {
        navController.navigate(
            Route.Canvas(memoId = memoId)
        )
    }

    fun navigateToMemoEdit() {
        navController.navigate(Route.MemoEdit)
    }

    fun navigatePopBackStack() = navController.popBackStack()
}

@SuppressLint("ComposableNaming")
@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}