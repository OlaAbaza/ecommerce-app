package com.example.shopy.ui.shopTab

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.custom_product.ProductsList
import com.example.shopy.ui.customerAddress.AddressViewModel
import com.example.shopy.ui.profileFragment.ProfileViewModel
import com.example.shopy.utils.CoroutineTestRule
import com.example.shopy.ui.shopTab.ShopTabViewModel
import com.example.shopy.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import io.mockk.*
import org.mockito.Mockito


@ExperimentalCoroutinesApi
class ShopTabViewModelTest {

    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    private lateinit var repository: IRepository
    private lateinit var viewModel: ShopTabViewModel
    private lateinit var applicationMock: Application

    var womanProducts = ProductsList(listOf(Product("ADIDAS | KID'S STAN SMITH","ADIDAS")))
    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        applicationMock = Mockito.mock(Application::class.java)

    }

    @Test
    fun testWhenViewModelInit_VerifyFetchWomanProductsCalled() {
        // Given
        coEvery { repository.getWomanProductsList() } returns mockk()

        // When
        viewModel = ShopTabViewModel(repository, applicationMock)

        // Then
        coVerify { repository.getWomanProductsList()}
    }

    @Test
    fun testWhenViewModelInit_VerifyFetchMenProductsDataCalled() {
        // Given
        coEvery { repository.getMenProductsList() } returns mockk()

        // When
        viewModel = ShopTabViewModel(repository, applicationMock)

        // Then
        coVerify { repository.getMenProductsList()}
    }

    @Test
    fun testWhenViewModelInit_VerifyFetchKidsProductsDataCalled() {
        // Given
        coEvery { repository.getKidsProductsList() } returns mockk()

        // When
        viewModel = ShopTabViewModel(repository, applicationMock)

        // Then
        coVerify { repository.getKidsProductsList()}
    }

    @Test
    fun testWhenViewModelInit_VerifyFetchOnSaleProductsDataCalled() {
        // Given
        coEvery { repository.getOnSaleProductsList() } returns mockk()

        // When
        viewModel = ShopTabViewModel(repository, applicationMock)

        // Then
        coVerify { repository.getOnSaleProductsList()}
    }

    @Test
    fun testWhenViewModelInit_VerifyFetchAllProductsDataCalled() {
        // Given
        coEvery { repository.getAllProductsList() } returns mockk()

        // When
        viewModel = ShopTabViewModel(repository, applicationMock)

        // Then
        coVerify { repository.getAllProductsList()}
    }

    @Test
    fun testWhenViewModelInit_VerifyFetchAllDiscountCodesDataCalled() {
        // Given
        coEvery { repository.getAllDiscountCodeList()} returns mockk()

        // When
        viewModel = ShopTabViewModel(repository, applicationMock)

        // Then
        coVerify { repository.getAllDiscountCodeList()}
    }

    @Test
    fun testWhenWomanProductsReturnedSuccessfully_VerifyWomanProductsChanged() {
        coEvery {

            repository.getWomanProductsList()

        } returns MutableLiveData(womanProducts)

        viewModel = ShopTabViewModel(repository, applicationMock)

        Assert.assertEquals(womanProducts, viewModel.fetchWomanProductsList()?.getOrAwaitValue())
    }
    @Test
    fun testWhenWomanProductsReturnedError_VerifyShowErrorMessage() {
        coEvery {

            repository.getWomanProductsList()

        } returns null

        viewModel = ShopTabViewModel(repository, applicationMock)

        Assert.assertEquals(null, viewModel.fetchWomanProductsList()?.getOrAwaitValue())
    }

    @Test
    fun testShopTabViewModelFactory() {
        coEvery { repository.fetchAllProducts() } returns mockk()

        val viewModel =
            ViewModelFactory(repository, applicationMock).create(ShopTabViewModel::class.java)

        assert(viewModel is ShopTabViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testShopTabViewModelFactoryOfUnknownType_VerifyExceptionThrown() {
        ViewModelFactory(repository, applicationMock).create(ShopTabViewModel::class.java)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}

