package com.example.popularlibs_homrworks.presenter

import android.util.Log
import com.example.popularlibs_homrworks.Screens
import com.example.popularlibs_homrworks.view.fragments.UsersView
import com.example.popularlibs_homrworks.model.GithubUser
import com.example.popularlibs_homrworks.model.GithubUsersRepo
import com.example.popularlibs_homrworks.view.adapter.UserItemView
import com.example.popularlibs_homrworks.view.main.TAG
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router


//презентер для работы с фрагментом UsersFragment,  Router для навигации
class UsersPresenter(val usersRepo: GithubUsersRepo, val router: Router):
    MvpPresenter<UsersView>() {

    val usersListPresenter =  UsersListPresenter()

    class UsersListPresenter : IUserListPresenter {
        val users = mutableListOf<GithubUser>()
        override var itemClickListener: ((UserItemView) -> Unit)? = null

        override fun getCount() = users.size

        override fun bindView(view: UserItemView) {
            val user = users[view.pos]
            view.setLogin(user.login)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadData()

        usersListPresenter.itemClickListener = { itemView ->
            //переход на экран пользователя
           val login =  usersListPresenter.users[itemView.pos].login
            Log.d(TAG, "UsersPresenter itemClickListener login =$login")
            router.replaceScreen(Screens.UserScreen(login))
        }
    }

    fun loadData() {
        val users =  usersRepo.getUsers()
        usersListPresenter.users.addAll(users)
        viewState.updateList()
    }

    //Методичка:
    //Для обработки нажатия клавиши «Назад» добавлена функция backPressed(), возвращающая Boolean,
    // в которой мы передаем обработку выхода с экрана роутеру. Вообще, функции Presenter,
    // согласно парадигме, не должны ничего возвращать, но в данном случае приходится
    // идти на компромисс из-за недостатков фреймворка. Дело в том, что у фрагмента
    // нет своего коллбэка для обработки перехода назад и нам придется пробрасывать его из Activity.
    // А возвращаемое значение нужно, чтобы в Activity мы могли определить,
    // было ли событие нажатия поглощено фрагментом или нужно обработать его стандартным способом.
    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}