package com.example.shopy.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myapplication.SearchCategoryItemAdapter
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentShopSearchBinding
import com.example.shopy.datalayer.entity.allproducts.allProduct
import com.example.shopy.datalayer.localdatabase.room.RoomService

import com.example.shopy.ui.shopTab.ShopTabViewModel
import com.example.shopy.domainLayer.Utils
import com.example.shopy.ui.category.ItemsRecyclerClick
import kotlinx.android.synthetic.main.activity_main.*


class ShopSearchFragment : Fragment(),ItemsRecyclerClick {

    init {
        setHasOptionsMenu(true)

    }
    lateinit var products:List<allProduct>
    lateinit var sortedProducts:List<allProduct>
    var productFilter=""
    lateinit var  shopTabViewModel : ShopTabViewModel

    private var _binding: FragmentShopSearchBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().toolbar_title.text = "Search"
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.GONE

        if(Utils.toolbarImg.visibility == View.VISIBLE){
            Utils.toolbarImg.visibility = View.GONE
        }
        if(Utils.cartView.visibility == View.VISIBLE){
            Utils.cartView.visibility = View.GONE
        }
        val application = requireNotNull(this.activity).application
        val repository = Repository(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        shopTabViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(ShopTabViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()
        inflater.inflate(R.menu.fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem: MenuItem? = menu?.findItem(R.id.fragmentsearch)
        val searchView : SearchView? = searchItem?.actionView as SearchView
        searchView?.setFocusable(true)
        searchView?.setIconified(false)


        shopTabViewModel.fetchallProductsList().observe(viewLifecycleOwner,{
            Log.i("output","***********"+it.toString())
            products = it.products
            sortedProducts = products
            binding.itemsRecView.adapter=SearchCategoryItemAdapter(it.products,requireContext(),this)

        })


        binding.sortSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!!.getItemAtPosition(position).equals("Alphabetically")){
                    sortedProducts = products.sortedBy { it.title }
                    // binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
                }else if (parent!!.getItemAtPosition(position).equals("price: Low to High")){
                    sortedProducts = products.sortedBy { it.variants!!.get(0).price}
                    //binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
                }else if(parent!!.getItemAtPosition(position).equals("price: High to Low")){
                    sortedProducts = products.sortedByDescending { it.variants!!.get(0).price }
                    //binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
                }else if (parent!!.getItemAtPosition(position).equals("none")) {
                    binding.itemsRecView.adapter = SearchCategoryItemAdapter(products, requireContext(),this@ShopSearchFragment)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("hitler","NOTHING")
            }

        }

        binding.filterSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!!.getItemAtPosition(position).equals("shoes")){
                    productFilter="shoes"
                }
                else if (parent!!.getItemAtPosition(position).equals("shirts")){
                    productFilter="shirts"
                }
                else if (parent!!.getItemAtPosition(position).equals("accessories")){
                    productFilter="accessories"
                }
                else if (parent!!.getItemAtPosition(position).equals("none")){
                    productFilter=""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("output","***********iiii")

                var filteredProducts = sortedProducts.filter { it.title!!.contains(query?:"none",true)&& it.productType!!.contains(productFilter,true)}
                binding.itemsRecView.adapter= SearchCategoryItemAdapter(filteredProducts,requireContext(),this@ShopSearchFragment)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("output","***********ppp")

                var filteredProducts = sortedProducts.filter { it.title!!.contains(newText?:"none",true)&& it.productType!!.contains(productFilter,true) }//&& it.productType.equals("shoes",true)}
                binding.itemsRecView.adapter= SearchCategoryItemAdapter(filteredProducts,requireContext(),this@ShopSearchFragment)
                return true
            }

        })

    }

    override fun itemOnClick(itemId: Long) {
        val action = NavGraphDirections.actionGlobalProuductDetailsFragment(itemId)
        findNavController().navigate(action)
    }
}




