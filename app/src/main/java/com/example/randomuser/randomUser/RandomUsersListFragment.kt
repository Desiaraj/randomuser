package com.example.randomuser.randomUser

import android.os.Bundle
import android.util.Log
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


class RandomUsersListFragment : Fragment(), RandomUserDataContract.View {

    lateinit var fragmentrandom: FragmentRandomUsersListBinding
    lateinit var randomUserDataPresenter: RandomUserDataPresenter
    var initialcount = 25;
    var isLoading = false;
    private var randomuserList: ArrayList<RandomUserDataClass> = ArrayList()
    private var searcharrayList: ArrayList<RandomUserDataClass> = ArrayList()
    lateinit var randomUserListAdapter: RandomUserListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentrandom =
            DataBindingUtil.inflate(inflater, R.layout.fragment_random_users_list, container, false)
        fragmentrandom.shimmerUserdata.startShimmer()
        randomUserDataPresenter = RandomUserDataPresenter(this)
        randomUserListAdapter =
            RandomUserListAdapter(activity as RandomUserDataActivity, randomuserList, this)
        fragmentrandom.rvUserslist.adapter = randomUserListAdapter
        return fragmentrandom.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        randomuserList.clear()
        initialcount = 25
        randomUserDataPresenter.getRandomUserDetail(initialcount)
        Glide.with(this).asGif().load(R.drawable.loader).into(fragmentrandom.imgLoader)
        fragmentrandom.rvUserslist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutmanager = recyclerView.layoutManager as LinearLayoutManager
                val totalitem = layoutmanager.itemCount
                val lastvisible = layoutmanager.findLastVisibleItemPosition()
                val endreached = lastvisible + 1 >= totalitem
                if (totalitem > 0 && endreached) {
                    if (!isLoading && searcharrayList.size == 0) {
                        isLoading = true
                        fragmentrandom.imgLoader.visibility = View.VISIBLE
                        initialcount += 25
                        randomUserDataPresenter.getRandomUserDetail(initialcount)
                    }
                }
            }
        })

        fragmentrandom.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        fragmentrandom.svSearch.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                initialcount = 25;
                randomuserList.clear()
                searcharrayList.clear()
                randomUserListAdapter.notifyDataSetChanged()
                randomUserDataPresenter.getRandomUserDetail(initialcount)
                return false
            }

        })
    }

    fun filterList(text: String) {
        searcharrayList.clear()
        randomuserList.forEach { item ->
            if (item.name?.first?.contains(text)!! || item.name.last?.contains(text)!! ||
                item.email?.contains(text)!! || item.location?.country?.contains(text)!! ||
                item.location.state?.contains(text)!!
            ) {
                searcharrayList.add(item)
            }
        }
        if (searcharrayList.size > 0) {
            randomuserList.clear()
            randomuserList.addAll(searcharrayList)
            randomUserListAdapter.notifyDataSetChanged()
        }
    }

    override fun getUserDetail(userdata: List<RandomUserDataClass>) {
        if (initialcount > 25) {
            isLoading = false
            fragmentrandom.imgLoader.visibility = View.GONE
        }
        randomuserList.clear()
        randomuserList.addAll(userdata)
        randomUserListAdapter.submitList(randomuserList)
        randomUserListAdapter.notifyDataSetChanged()
        fragmentrandom.shimmerUserdata.stopShimmer()
        fragmentrandom.shimmerUserdata.visibility = View.GONE
        fragmentrandom.rlHome.visibility = View.VISIBLE
    }


    override fun getUserDataFailure(msg: String) {
        Log.d("RandomUser", "Failure Message $msg")
    }

    override fun userClicked(data: RandomUserDataClass) {
        val bundle = bundleOf("data" to data)
        getNavController()?.navigate(
            R.id.action_randomUsersListFragment_to_randomUserDetailFragment,
            bundle
        )
    }

    private fun getNavController(): NavController? {
        val fragment: Fragment? =
            activity?.supportFragmentManager?.findFragmentById(R.id.my_navhost_fragment)
        check(fragment is NavHostFragment) { "Activity $this does not have a NavHostFragment" }
        return fragment.navController
    }
}

