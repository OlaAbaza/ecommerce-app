/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.shopy.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.CategoriesViewModel
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.ui.profileFragment.ProfileViewModel
import com.example.shopy.ui.allWishListFragment.AllWishListViewModel
import com.example.shopy.ui.customerAddress.AddressViewModel
import com.example.shopy.ui.displayOrderFragment.DisplayOrderViewModel
import com.example.shopy.ui.mainActivity.MainActivityViewModel
import com.example.shopy.ui.meScreen.MeViewModel
import com.example.shopy.ui.payment.PaymentViewModel
import com.example.shopy.ui.productDetailsActivity.ProductDetailsViewModel
import com.example.shopy.ui.settings.SettingsViewModel
import com.example.shopy.ui.shopTab.ShopTabViewModel
import com.example.shopy.ui.shoppingBag.OrderViewModel
import com.example.shopy.ui.showOneOrderDetails.ShowOneOrderDetailsViewModel
import com.example.shopy.ui.signIn.SignInViewModel

class ViewModelFactory(
    private val repositoryImpl: IRepository,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(repositoryImpl,application) as T
        }
        else if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            return AddressViewModel(repositoryImpl,application) as T
        }
        else if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(repositoryImpl,application) as T
        }
        else if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(repositoryImpl,application) as T
        }
        else if (modelClass.isAssignableFrom(AllWishListViewModel::class.java)) {
            return AllWishListViewModel(repositoryImpl,application) as T
        }
        else if (modelClass.isAssignableFrom(MeViewModel::class.java)) {
            return MeViewModel(repositoryImpl,application) as T
        }
        else if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel(repositoryImpl,application) as T
        }
        else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repositoryImpl,application) as T
        }
        else if (modelClass.isAssignableFrom(DisplayOrderViewModel::class.java)) {
            return DisplayOrderViewModel(repositoryImpl,application) as T
        }
        else if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(repositoryImpl,application) as T
        }
        else if(modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(repositoryImpl, application) as T
        }
        else if(modelClass.isAssignableFrom(ShopTabViewModel::class.java)) {
            return ShopTabViewModel(repositoryImpl, application) as T
        }
        else if(modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel(repositoryImpl, application) as T
        }
        else if(modelClass.isAssignableFrom(ShowOneOrderDetailsViewModel::class.java)) {
            return ShowOneOrderDetailsViewModel(repositoryImpl, application) as T
        }
        else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

