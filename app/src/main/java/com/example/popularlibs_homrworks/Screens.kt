package com.example.popularlibs_homrworks

import com.example.popularlibs_homrworks.view.fragments.UserFragment
import com.example.popularlibs_homrworks.view.fragments.UsersFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class UsersScreen() : SupportAppScreen() {
        override fun getFragment() = UsersFragment.newInstance()
    }
    class UserScreen(val login: String) : SupportAppScreen() {
        override fun getFragment() = UserFragment.newInstance(login)
    }
}