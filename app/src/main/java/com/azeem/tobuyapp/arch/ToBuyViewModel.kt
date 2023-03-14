package com.azeem.tobuyapp.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azeem.tobuyapp.Event
import com.azeem.tobuyapp.database.AppDatabase
import com.azeem.tobuyapp.database.entity.CategoryEntity
import com.azeem.tobuyapp.database.entity.ItemEntity
import com.azeem.tobuyapp.database.entity.ItemWithCategoryEntity
import com.azeem.tobuyapp.getHeaderTextForPriority
import kotlinx.coroutines.launch

class ToBuyViewModel : ViewModel() {

    private lateinit var repository: ToBuyRepository

    var transactionCompleteLiveData = MutableLiveData<Event<Boolean>>()

    val categoryEntitiesLiveData = MutableLiveData<List<CategoryEntity>>()

    val itemWithCategoryEntityLiveData = MutableLiveData<List<ItemWithCategoryEntity>>()

    private val _categoriesViewStateLiveData = MutableLiveData<CategoriesViewState>()
    val categoriesViewStateLiveData: LiveData<CategoriesViewState>
        get() = _categoriesViewStateLiveData


    //home page
    var currentSort: HomeViewState.Sort = HomeViewState.Sort.NONE
        set(value) {
            field = value
            updateHomeViewState(itemWithCategoryEntityLiveData.value!!)
        }

    private val _homeViewStateLiveData = MutableLiveData<HomeViewState>()
    val homeViewStateLiveData: LiveData<HomeViewState>
        get() = _homeViewStateLiveData


    fun init(appDatabase: AppDatabase) {
        repository = ToBuyRepository(appDatabase)

        viewModelScope.launch {
            repository.getAllCategory().collect { categories ->
                categoryEntitiesLiveData.postValue(categories)
            }
        }
        viewModelScope.launch {
            repository.getAllItemWithCategoryEntities().collect { items ->
                itemWithCategoryEntityLiveData.postValue(items)
                updateHomeViewState(items)
            }
        }
    }

    private fun updateHomeViewState(items: List<ItemWithCategoryEntity>) {

        val dataList = ArrayList<HomeViewState.DataItem<*>>()
        when (currentSort) {
            HomeViewState.Sort.NONE -> {
                var currentPriority: Int = -1
                items.sortedByDescending { it.itemEntity.priority }.forEach { item ->
                    if (item.itemEntity.priority != currentPriority) {
                        currentPriority = item.itemEntity.priority
                        val headerItem = HomeViewState.DataItem(
                            data = getHeaderTextForPriority(currentPriority),
                            isHeader = true
                        )
                        dataList.add(headerItem)
                    }
                    val dataItem = HomeViewState.DataItem(data = item)
                    dataList.add(dataItem)
                }
            }



            HomeViewState.Sort.CATEGORY -> {
                var currentCategoryId = "no_id"
                items.sortedBy {
                    it.categoryEntity?.name ?: CategoryEntity.DEFAULT_CATEGORY_ID
                }.forEach { item ->
                    if (item.itemEntity.categoryId != currentCategoryId) {
                        currentCategoryId = item.itemEntity.categoryId
                        val headerItem = HomeViewState.DataItem(
                            data = item.categoryEntity?.name ?: CategoryEntity.DEFAULT_CATEGORY_ID,
                            isHeader = true
                        )
                        dataList.add(headerItem)

                    }
                    val dataItem = HomeViewState.DataItem(data = item)
                    dataList.add(dataItem)

                }

            }
            HomeViewState.Sort.OLDEST -> {
                val headerItem = HomeViewState.DataItem(
                    data = "Oldest",
                    isHeader = true
                )
                dataList.add(headerItem)
                items.sortedBy { it.itemEntity.createdAt }.forEach {
                    val dataItem = HomeViewState.DataItem(data = it)
                    dataList.add(dataItem)
                }

            }
            HomeViewState.Sort.NEWEST -> {
                val headerItem = HomeViewState.DataItem(
                    data = "Newest",
                    isHeader = true
                )
                dataList.add(headerItem)
                items.sortedByDescending { it.itemEntity.createdAt }.forEach {
                    val dataItem = HomeViewState.DataItem(data = it)
                    dataList.add(dataItem)
                }
            }
        }
        _homeViewStateLiveData.postValue(
            HomeViewState(
                dataList = dataList,
                isLoading = false,
                sort = currentSort
            )
        )
    }

    fun onCategorySelected(categoryId: String, showLoading: Boolean = false) {
        if (showLoading) {
            val loadingViewState = CategoriesViewState(isLoading = true)
            _categoriesViewStateLiveData.value = loadingViewState
        }

        val categories = categoryEntitiesLiveData.value ?: return
        val viewStateItemList = ArrayList<CategoriesViewState.Item>()
        viewStateItemList.add(
            CategoriesViewState.Item(
                categoryEntity = CategoryEntity.getDefaultCategory(),
                isSelected = categoryId == CategoryEntity.DEFAULT_CATEGORY_ID
            )
        )

        categories.forEach {
            viewStateItemList.add(
                CategoriesViewState.Item(
                    categoryEntity = it,
                    isSelected = it.id == categoryId
                )
            )
        }

        val viewState = CategoriesViewState(itemList = viewStateItemList)
        _categoriesViewStateLiveData.postValue(viewState)

    }


    data class HomeViewState(
        val dataList: List<DataItem<*>> = emptyList(),
        val isLoading: Boolean = false,
        val sort: Sort = Sort.NONE
    ) {
        data class DataItem<T>(
            val data: T,
            val isHeader: Boolean = false
        )

        enum class Sort(val displayName: String) {
            NONE("None"),
            CATEGORY("Category"),
            OLDEST("Oldest"),
            NEWEST("Newest")
        }
    }


    data class CategoriesViewState(
        val isLoading: Boolean = false,
        val itemList: List<Item> = emptyList()
    ) {
        data class Item(val categoryEntity: CategoryEntity = CategoryEntity(),
            val isSelected: Boolean = false
        )

        fun getSelectedCategoryId(): String {
            return itemList.find { it.isSelected }?.categoryEntity?.id
                ?: CategoryEntity.DEFAULT_CATEGORY_ID
        }
    }


    fun insertItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.insertItem(itemEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }

    }


    fun deleteItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.deleteItem(itemEntity)
        }

    }

    fun updateItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            repository.updateItem(itemEntity)
        }
    }

    fun insertCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.insertCategory(categoryEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }

    }

    fun deleteCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.deleteCategory(categoryEntity)
        }

    }

    fun updateCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(categoryEntity)
            transactionCompleteLiveData.postValue(Event(true))
        }
    }


}