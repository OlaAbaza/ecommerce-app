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
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.ui.allWishListFragment.AllWishListViewModel
import com.example.shopy.ui.customerAddress.AddressViewModel
import com.example.shopy.ui.displayOrderFragment.DisplayOrderViewModel
import com.example.shopy.ui.mainActivity.MainActivityViewModel
import com.example.shopy.ui.meScreen.MeViewModel
import com.example.shopy.ui.productDetailsActivity.ProductDetailsViewModel
import com.example.shopy.ui.shoppingBag.OrderViewModel
import com.example.shopy.ui.signIn.SignInViewModel

class ViewModelFactory(
    private val repository: Repository,
    private val remoteDataSource: RemoteDataSourceImpl,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(remoteDataSource,application) as T
        }
        if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            return AddressViewModel(remoteDataSource,application) as T
        }
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(repository,application) as T
        }
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(repository,application) as T
        }
        if (modelClass.isAssignableFrom(AllWishListViewModel::class.java)) {
            return AllWishListViewModel(repository,application) as T
        }
        if (modelClass.isAssignableFrom(MeViewModel::class.java)) {
            return MeViewModel(repository,application) as T
        }
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel(repository,application) as T
        }
        if (modelClass.isAssignableFrom(DisplayOrderViewModel::class.java)) {
            return DisplayOrderViewModel(repository,application) as T
        }
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(repository,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

