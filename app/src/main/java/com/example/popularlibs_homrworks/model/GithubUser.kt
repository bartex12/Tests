package com.example.popularlibs_homrworks.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Аннотация @Parcelize говорит о необходимости сгенерировать весь boilerplate-код,
// необходимый для работы Parcelable, автоматически, избавляя нас от рутины его написания вручную.
@Parcelize
data class GithubUser(val login:String):Parcelable {
}