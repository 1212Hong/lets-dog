//package com.dog.di
//
//import com.dog.data.viewmodel.user.UserViewModel
//import com.dog.util.common.UserService
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ViewModelComponent
//
//@InstallIn(ViewModelComponent::class)
//@Module
//object UserModule {
//
//    @Provides
//    fun provideUserViewModel(userService: UserService): UserViewModel {
//        return UserViewModel(userService)
//    }
//}
