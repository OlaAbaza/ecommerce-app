package com.example.shopy.ui.allWishListFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentAllWishListBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.RoomService
import kotlinx.android.synthetic.main.activity_main.*

class AllWishListFragment : Fragment() {

    private lateinit var bindingAllWishListFragment: FragmentAllWishListBinding
    private lateinit var allWishListFragmentViewModel: AllWishListViewModel
    private lateinit var withListAdapter: WishListAdapter
    private lateinit var wishListData: List<Product>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeToolbar()

        bindingAllWishListFragment = FragmentAllWishListBinding.inflate(inflater, container, false)


        val repository = RepositoryImpl(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))
        )
        val viewModelFactory = ViewModelFactory(repository, requireActivity().application)

        allWishListFragmentViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[AllWishListViewModel::class.java]



        wishListData = ArrayList()

        withListAdapter =
            WishListAdapter(
                wishListData,
                allWishListFragmentViewModel.intentTOProductDetails,
                allWishListFragmentViewModel.deleteItem
            )
        bindingAllWishListFragment.wishRecyclerView.apply {
            this.adapter = withListAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)
        }

        allWishListFragmentViewModel.getAllWishList().observe(requireActivity(), {
            if (it.isEmpty()) {
                bindingAllWishListFragment.emptyAnimationView.visibility = View.VISIBLE
                bindingAllWishListFragment.emptyText.visibility = View.VISIBLE
            } else {
                bindingAllWishListFragment.emptyText.visibility = View.GONE
                bindingAllWishListFragment.emptyAnimationView.visibility = View.GONE
            }

            wishListData = it
            withListAdapter.productList = wishListData
            withListAdapter.notifyDataSetChanged()
        })


        allWishListFragmentViewModel.intentTOProductDetails.observe(requireActivity(), {

            if (it != null) {
                val action = NavGraphDirections.actionGlobalProuductDetailsFragment(it.id)
                findNavController().navigate(action)

            }
        })

        allWishListFragmentViewModel.deleteItem.observe(viewLifecycleOwner, {
            deleteAlert(it.id)
        })

        return bindingAllWishListFragment.root
    }

    private fun deleteAlert(id: Long) {

        val builder = AlertDialog.Builder(requireContext())
        //  builder.setTitle(De)
        builder.setTitle(getString(R.string.Delete_Item_From_Wish_List))
        builder.setMessage(getString(R.string.are_you_sure))

        builder.setPositiveButton("Delete") { _, _ ->
            allWishListFragmentViewModel.deleteOneItemFromWishList(id)
            allWishListFragmentViewModel.deleteItem = MutableLiveData<Product>()
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            allWishListFragmentViewModel.deleteItem = MutableLiveData<Product>()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK)
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY)
    }

    private fun changeToolbar() {
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.GONE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.searchIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.settingIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.GONE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.VISIBLE
        requireActivity().toolbar_title.setTextColor(Color.WHITE)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        requireActivity().toolbar_title.text = getString(R.string.AllWishList)
    }

    override fun onStop() {
        super.onStop()
        allWishListFragmentViewModel.intentTOProductDetails = MutableLiveData<Product>()
    }
}