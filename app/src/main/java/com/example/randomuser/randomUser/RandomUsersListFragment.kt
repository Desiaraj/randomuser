package com.example.randomuser.randomUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomuser.R
import com.example.randomuser.databinding.FragmentRandomUsersListBinding
import com.example.randomuser.network.RandomUserDataClass
import com.google.android.material.snackbar.Snackbar

/**
 * Created by desiaraj on 18/07/2021
 */

class RandomUsersListFragment : Fragment(), RandomUserDataContract.View {

    lateinit var fragmentRandom: FragmentRandomUsersListBinding
    lateinit var randomUserDataPresenter: RandomUserDataPresenter
    var initialCount = 25
    var isLoading = false
    private var randomUserList: ArrayList<RandomUserDataClass> = ArrayList()
    private var searchArrayList: ArrayList<RandomUserDataClass> = ArrayList()
    private lateinit var randomUserListAdapter: RandomUserListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentRandom =
            DataBindingUtil.inflate(inflater, R.layout.fragment_random_users_list, container, false)
        fragmentRandom.shimmerUserdata.startShimmer()
        randomUserDataPresenter = RandomUserDataPresenter(this)
        randomUserListAdapter =
            RandomUserListAdapter(activity as RandomUserDataActivity, randomUserList, this)
        fragmentRandom.rvUserslist.adapter = randomUserListAdapter
        return fragmentRandom.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        randomUserList.clear()
        initialCount = 25
        randomUserDataPresenter.getRandomUserDetail(initialCount)
        Glide.with(this).asGif().load(R.drawable.loader).into(fragmentRandom.imgLoader)
        fragmentRandom.rvUserslist.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItem = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val endReached = lastVisible + 1 >= totalItem
                if (totalItem > 0 && endReached) {
                    if (!isLoading && searchArrayList.size == 0) {
                        isLoading = true
                        fragmentRandom.imgLoader.visibility = View.VISIBLE
                        initialCount += 25
                        randomUserDataPresenter.getRandomUserDetail(initialCount)
                    }
                }
            }
        })

        fragmentRandom.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    filterList(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        fragmentRandom.svSearch.setOnCloseListener {
            initialCount = 25
            randomUserList.clear()
            searchArrayList.clear()
            randomUserListAdapter.notifyDataSetChanged()
            randomUserDataPresenter.getRandomUserDetail(initialCount)
            false
        }
    }

    fun filterList(text: String) {
        searchArrayList.clear()
        randomUserList.forEach { item ->
            if (item.name?.first?.contains(text)!! || item.name.last?.contains(text)!! ||
                item.email?.contains(text)!! || item.location?.country?.contains(text)!! ||
                item.location.state?.contains(text)!!
            ) {
                searchArrayList.add(item)
            }
        }
        if (searchArrayList.size > 0) {
            randomUserList.clear()
            randomUserList.addAll(searchArrayList)
            randomUserListAdapter.notifyDataSetChanged()
        }
    }

    override fun getUserDetail(userdata: List<RandomUserDataClass>) {
        if (initialCount > 25) {
            isLoading = false
            fragmentRandom.imgLoader.visibility = View.GONE
        }
        randomUserList.clear()
        randomUserList.addAll(userdata)
        randomUserListAdapter.submitList(randomUserList)
        randomUserListAdapter.notifyDataSetChanged()
        fragmentRandom.shimmerUserdata.stopShimmer()
        fragmentRandom.shimmerUserdata.visibility = View.GONE
        fragmentRandom.rlHome.visibility = View.VISIBLE
    }


    override fun getUserDataFailure(msg: String) {
       Snackbar.make(fragmentRandom.rlHome,msg,1000).show()
    }

    override fun userClicked(data: RandomUserDataClass) {
        val bundle = bundleOf("data" to data)
        getNavController().navigate(
            R.id.action_randomUsersListFragment_to_randomUserDetailFragment,
            bundle
        )
    }

    private fun getNavController(): NavController {
        val fragment: Fragment? =
            activity?.supportFragmentManager?.findFragmentById(R.id.my_navhost_fragment)
        check(fragment is NavHostFragment) { "Activity $this does not have a NavHostFragment" }
        return fragment.navController
    }
}

