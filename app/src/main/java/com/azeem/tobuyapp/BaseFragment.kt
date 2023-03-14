package com.azeem.tobuyapp

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.azeem.tobuyapp.arch.ToBuyViewModel
import com.azeem.tobuyapp.database.AppDatabase
import com.azeem.tobuyapp.ui.activities.MainActivity

abstract class BaseFragment:Fragment() {
    protected val mainActivity: MainActivity
        get() = activity as MainActivity
    protected val appDatabase:AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())
    protected val shareViewModel:ToBuyViewModel by activityViewModels()
    protected fun navigationUp(){
        mainActivity.navController.navigateUp()
    }
    protected fun navigationViewNavGraph(actionId:Int){
        mainActivity.navController.navigate(actionId)
    }
    protected fun navigationViewNavGraph(navDirections: NavDirections){
        mainActivity.navController.navigate(navDirections)
    }

    protected fun View.showKeyboard() =
        ViewCompat.getWindowInsetsController(this)?.show(WindowInsetsCompat.Type.ime())



//    protected val navController by lazy {
//        (activity as MainActivity).navController
//    }

}