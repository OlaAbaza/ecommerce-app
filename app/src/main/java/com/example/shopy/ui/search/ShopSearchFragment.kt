package com.example.shopy.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.SearchCategoryItemAdapter
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentShopSearchBinding
import com.example.shopy.datalayer.entity.allproducts.allProduct
import com.example.shopy.datalayer.localdatabase.room.RoomService

import com.example.shopy.ui.shopTab.ShopTabViewModel
import com.example.shopy.ui.category.ItemsRecyclerClick
import kotlinx.android.synthetic.main.activity_main.*


class ShopSearchFragment : Fragment(),ItemsRecyclerClick {

//    init {
//        setHasOptionsMenu(true)
//
//    }
    lateinit var products:List<allProduct>
    lateinit var sortedProducts:List<allProduct>
    var productFilter=""
    lateinit var  shopTabViewModel : ShopTabViewModel
    var queryText=""
    private var _binding: FragmentShopSearchBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var menu: Menu
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        changeToolbar()

        val application = requireNotNull(this.activity).application
        val repository = RepositoryImpl(
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
      val mainSearchView=  requireActivity().findViewById<SearchView>(R.id.mainSearchView)

        mainSearchView?.isFocusable = true
        mainSearchView?.isIconified = false


        shopTabViewModel.fetchallProductsList().observe(viewLifecycleOwner,{
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
                if (!parent!!.getItemAtPosition(position).equals("SORT")) {
                    showData()
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
                else if (parent!!.getItemAtPosition(position).equals("t-shirts")){
                    productFilter="t-shirts"
                }
                else if (parent!!.getItemAtPosition(position).equals("accessories")){
                    productFilter="accessories"
                }
                else if (parent!!.getItemAtPosition(position).equals("none")){
                    productFilter=""
                }
                if (!parent!!.getItemAtPosition(position).equals("FILTER")) {
                    showData()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        mainSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                queryText=query?:""
                showData()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                queryText=newText?:""
                showData()
                return true
            }

        })
    }


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//
//
//        menu.clear()
//        this.menu=menu
//        inflater.inflate(R.menu.fragment_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//        val searchItem: MenuItem? = menu?.findItem(R.id.fragmentsearch)
//        val searchView : SearchView? = searchItem?.actionView as SearchView
//        searchView?.setFocusable(true)
//        searchView?.setIconified(false)
//
//
//        shopTabViewModel.fetchallProductsList().observe(viewLifecycleOwner,{
//            products = it.products
//            sortedProducts = products
//            binding.itemsRecView.adapter=SearchCategoryItemAdapter(it.products,requireContext(),this)
//
//        })
//
//
//        binding.sortSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                if (parent!!.getItemAtPosition(position).equals("Alphabetically")){
//                    sortedProducts = products.sortedBy { it.title }
//                    // binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
//                }else if (parent!!.getItemAtPosition(position).equals("price: Low to High")){
//                    sortedProducts = products.sortedBy { it.variants!!.get(0).price}
//                    //binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
//                }else if(parent!!.getItemAtPosition(position).equals("price: High to Low")){
//                    sortedProducts = products.sortedByDescending { it.variants!!.get(0).price }
//                    //binding.itemsRecView.adapter=SearchCategoryItemAdapter(filteredProducts,requireContext())
//                }else if (parent!!.getItemAtPosition(position).equals("none")) {
//                    binding.itemsRecView.adapter = SearchCategoryItemAdapter(products, requireContext(),this@ShopSearchFragment)
//                }
//                if (!parent!!.getItemAtPosition(position).equals("SORT")) {
//                    showData()
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                Log.d("hitler","NOTHING")
//            }
//
//        }
//
//        binding.filterSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                if (parent!!.getItemAtPosition(position).equals("shoes")){
//                    productFilter="shoes"
//                }
//                else if (parent!!.getItemAtPosition(position).equals("t-shirts")){
//                    productFilter="t-shirts"
//                }
//                else if (parent!!.getItemAtPosition(position).equals("accessories")){
//                    productFilter="accessories"
//                }
//                else if (parent!!.getItemAtPosition(position).equals("none")){
//                    productFilter=""
//                }
//                if (!parent!!.getItemAtPosition(position).equals("FILTER")) {
//                    showData()
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//        }
//
//        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                queryText=query?:""
//                showData()
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                queryText=newText?:""
//                showData()
//                return true
//            }
//
//        })
//
//    }

    private fun showData(){
        var filteredProducts = sortedProducts.filter { it.title!!.contains(queryText?:"none",true)&& it.productType!!.contains(productFilter,true)}
        if(filteredProducts.size!=0) {
            binding.placeHolder.visibility=View.GONE
            binding.itemsRecView.adapter = SearchCategoryItemAdapter(
                filteredProducts,
                requireContext(),
                this@ShopSearchFragment
            )
        }
        else{
            binding.itemsRecView.adapter = SearchCategoryItemAdapter(
                filteredProducts,
                requireContext(),
                this@ShopSearchFragment
            )
            binding.placeHolder.visibility=View.VISIBLE

        }
    }

    override fun itemOnClick(itemId: Long) {
        val action = NavGraphDirections.actionGlobalProuductDetailsFragment(itemId)
        findNavController().navigate(action)
    }
    private fun changeToolbar() {
        requireActivity().findViewById<SearchView>(R.id.mainSearchView).visibility=View.VISIBLE
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.GONE
        requireActivity().toolbar.visibility = View.VISIBLE

        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.black_arrow))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }
        //requireActivity().bottom_nav.visibility = View.VISIBLE
        requireActivity().toolbar_title.text = ""
        requireActivity().findViewById<View>(R.id.searchIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.INVISIBLE
    }

}




