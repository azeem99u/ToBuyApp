package com.azeem.tobuyapp.database.dao

import androidx.room.*
import com.azeem.tobuyapp.database.entity.CategoryEntity
import com.azeem.tobuyapp.database.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryEntityDao {

        @Query("select * from category_entity")
        fun getAllCategories(): Flow<List<CategoryEntity>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(categoryEntity: CategoryEntity)

        @Delete
        suspend fun delete(categoryEntity: CategoryEntity)

        @Update
        suspend fun update(categoryEntity: CategoryEntity)
}