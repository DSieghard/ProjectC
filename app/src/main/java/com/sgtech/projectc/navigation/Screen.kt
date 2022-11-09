package com.sgtech.projectc.navigation

import com.sgtech.projectc.core.Constants.AUTH_SCREEN
import com.sgtech.projectc.core.Constants.PROFILE_SCREEN

sealed class Screen(val route: String){
    object AuthScreen: Screen(AUTH_SCREEN)
    object ProfileScreen: Screen(PROFILE_SCREEN)
}