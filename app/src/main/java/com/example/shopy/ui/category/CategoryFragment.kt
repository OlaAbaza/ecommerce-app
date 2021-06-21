package com.example.shopy.ui.category

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.*
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentCategoryBinding
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.localdatabase.room.RoomService
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.cart_toolbar_view.view.*
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_category.shimmerFrameLayout1
import kotlinx.android.synthetic.main.fragment_category.shimmerFrameLayout2
import kotlinx.android.synthetic.main.fragment_kids_product.*
import kotlinx.android.synthetic.main.fragment_woman_products.*
import kotlinx.android.synthetic.main.list_toolbar_view.view.*


class CategoryFragment : Fragment(), MainCategoryRecyclerClick, SubCategoryRecyclerClick,ItemsRecyclerClick {
    var mainCategoryIndex=0
    var subCategoryIndex=0
    var colID:Long=268359696582
    lateinit var catViewModel:CategoriesViewModel
    lateinit var products:List<Product>
    lateinit var subList:List<Product>
    lateinit var  subcatList:Array<String>
    private var _binding: FragmentCategoryBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application
        val remoteDataSource = RemoteDataSourceImpl()
        val repository = RepositoryImpl(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        catViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(CategoriesViewModel::class.java)

        Log.i("output","one")
        return view
    }




    override fun onSubClick(position: Int) {
        super.onSubClick(position)
        Log.i("output","two")
        subCategoryIndex=position
        subList=getSubCategoryItems(position)
        if (subList.size!=0) {
            binding.placeHolder.visibility=View.GONE
            binding.itemsRecView.adapter = CategoryItemAdapter(subList, requireContext(), this)
        }else{
            binding.itemsRecView.adapter = CategoryItemAdapter(subList, requireContext(), this)
            binding.placeHolder.visibility=View.VISIBLE
        }
        binding.subcategoriesRecView.adapter!!.notifyDataSetChanged()
    }
    override fun onMainClick(position: Int) {
        super.onMainClick(position)
        Log.i("output","three")

        binding.subcategoriesRecView.adapter=SubCategoriesAdapter(subcatList,this)
        binding.subcategoriesRecView.adapter!!.notifyDataSetChanged()
        mainCategoryIndex=position
        colID=getMainCategory(position)
        if (NetworkChangeReceiver.isOnline) {
            networkCatView.visibility = View.GONE
            category_constraintlayout.visibility = View.VISIBLE
            catViewModel.fetchCatProducts(colID).observe(requireActivity(), {
                products = it
                binding.placeHolder.visibility=View.GONE
                binding.itemsRecView.adapter = CategoryItemAdapter(products, requireContext(), this)
                Log.d("hitler", "list size: " + it.size)
                binding.itemsRecView.adapter!!.notifyDataSetChanged()
            })
        }else{
            networkCatView.visibility = View.VISIBLE
            category_constraintlayout.visibility = View.GONE

        }
        binding.mainCategoriesRecView.adapter!!.notifyDataSetChanged()

        /*binding.mainCategoriesRecView.findViewHolderForAdapterPosition(position)!!.itemView.underLine.background=
            ColorDrawable(Color.parseColor("#ffffff"))*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("output","five")

        binding.shimmerFrameLayout1.startShimmerAnimation()
        binding.shimmerFrameLayout2.startShimmerAnimation()
        binding.shimmerFrameLayout3.startShimmerAnimation()
        binding.shimmerFrameLayout4.startShimmerAnimation()
        changeToolbar()
        subcatList= arrayOf("Shoes","Accessories","T-Shirts")
        var mainCatList= arrayOf("Home","kids","Men","Sales","Women")
        binding.subcategoriesRecView.adapter= SubCategoriesAdapter(subcatList,this)
        binding.mainCategoriesRecView.adapter= MainCategoriesAdapter(mainCatList,this)
        if (NetworkChangeReceiver.isOnline) {

            networkCatView.visibility = View.GONE
            category_constraintlayout.visibility = View.VISIBLE
            catViewModel.fetchCatProducts(267715608774).observe(requireActivity(), {
                if(it != null){
                    shimmerFrameLayout1.stopShimmerAnimation()
                    shimmerFrameLayout2.stopShimmerAnimation()
                    shimmerFrameLayout3.stopShimmerAnimation()
                    shimmerFrameLayout4.stopShimmerAnimation()

                    shimmerFrameLayout1.visibility = View.GONE
                    shimmerFrameLayout2.visibility = View.GONE
                    shimmerFrameLayout3.visibility = View.GONE
                    shimmerFrameLayout4.visibility = View.GONE

                    binding.itemsRecView.visibility = View.VISIBLE
                    products = it
                    binding.itemsRecView.adapter = CategoryItemAdapter(products, requireContext(), this)
                }

            })
        }else{
            networkCatView.visibility = View.VISIBLE
            category_constraintlayout.visibility = View.GONE
        }

    }

    fun getMainCategory(position:Int):Long{
        var main:Long=0
        when(position){
            0-> main=267715608774
            1-> main=268359663814
            2-> main=268359598278
            3-> main=268359696582
            4-> main=268359631046
            else-> main=0
        }
        return main
    }
    fun getSubCategoryItems(position:Int):List<Product>{
        lateinit var subCatList:List<Product>
        when(position){
            0-> subCatList=products.filter { it.productType.equals("SHOES")}
            1-> subCatList=products.filter { it.productType.equals("ACCESSORIES")}
            2-> subCatList=products.filter { it.productType.equals("T-SHIRTS")}
            else-> subCatList=products.filter { it.productType.equals("SHOES")}
        }
        return subCatList
    }

    override fun itemOnClick(itemId: Long) {
        val action = NavGraphDirections.actionGlobalProuductDetailsFragment(itemId)
        findNavController().navigate(action)
    }


    private fun changeToolbar() {
        requireActivity().findViewById<SearchView>(R.id.mainSearchView).visibility=View.GONE
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.VISIBLE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.favourite).favouriteButton.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().findViewById<View>(R.id.cartView).cartButton.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().settingIcon.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().findViewById<View>(R.id.searchIcon).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.settingIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.VISIBLE
        requireActivity().toolbar_title.setTextColor(Color.BLACK)
        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        requireActivity().toolbar.setNavigationIcon(null)
        requireActivity().toolbar_title.text = "Category"


    }

    override fun onDetach() {
        super.onDetach()
    }
}