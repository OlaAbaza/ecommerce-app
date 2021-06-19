package com.example.shopy.ui.productDetailsActivity

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.datalayer.entity.itemPojo.Image
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.datalayer.entity.itemPojo.ProductItem
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.localdatabase.room.cartBag.CartDao
import com.example.shopy.ui.meScreen.MeViewModel
import com.example.shopy.ui.meScreen.RxSchedulerRule
import com.example.shopy.utils.CoroutineTestRule
import io.mockk.*
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.Callable

class ProuductDetailsFragmentTest : TestCase() {



    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val taskRule = InstantTaskExecutorRule()

    @Rule
    var rxSchedulerRule = RxSchedulerRule()

    private lateinit var repository: IRepository
    private lateinit var applicationMock: Application
    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var ptouduact : ProductCartModule


        @Before
    public override fun setUp() {
        super.setUp()
        repository = mockk(relaxed = true)
        applicationMock = Mockito.mock(Application::class.java)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
        ptouduact=  ProductCartModule(1234567,"","","","","" +""
            ,"","","","","","","","","", emptyList()
            , emptyList(), emptyList(),Image(12345678951,12345678951,1,"",""
                ,"",1,2,"", emptyList(),""))
    }

    @Test
    fun testProductDetails(){
        coEvery { repository.getProuduct(6687367823558) } returns mockk()


        viewModel = ProductDetailsViewModel(repository, applicationMock)
        val observer=mockk<Observer<ProductItem>>()
        viewModel.getProductByIdFromNetwork(6687367823558)
        viewModel.observeProductDetails().observeForever(observer)

        coVerify  { viewModel.observeProductDetails() }

    }


    public override fun tearDown() {
        clearAllMocks()
    }
}