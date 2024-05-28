package com.example.natifetestapp.presentation.navigation

sealed class Routes {

    object MainScreenRoute: BaseRoute {

        override val authority = "main"
    }

    object DetailsScreenRoute: BaseRoute {

        const val keyInitialItemIndex = "initial_item_index"

        override val authority = "details"

        override fun route(): String {
            return buildRouteInternal(arguments = arrayOf(keyInitialItemIndex))
        }
    }
}