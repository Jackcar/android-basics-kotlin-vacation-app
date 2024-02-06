/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.vacation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.vacation.data.VacationItem
import com.project.vacation.data.ItemDao
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the Inventory repository and an up-to-date list of all items.
 *
 */
class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    // Cache all items form the database using LiveData.
    val allItems: LiveData<List<VacationItem>> = itemDao.getItems().asLiveData()

    /**
     * Returns true if stock is available to sell, false otherwise.
     */
    fun isStockAvailable(vacationItem: VacationItem): Boolean {
//        return (vacationItem.startDate > 0)
        return true
    }

    /**
     * Updates an existing Item in the database.
     */
    fun updateItem(
        itemId: Int,
        itemTitle: String,
        itemPlace: String,
        itemStartDate: String,
        itemEndDate: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemTitle, itemPlace, itemStartDate, itemEndDate)
        updateItem(updatedItem)
    }


    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateItem(vacationItem: VacationItem) {
        viewModelScope.launch {
            itemDao.update(vacationItem)
        }
    }

    /**
     * Decreases the stock by one unit and updates the database.
     */
    fun sellItem(vacationItem: VacationItem) {
//        if (vacationItem.startDate > 0) {
//            // Decrease the quantity by 1
//            val newItem = vacationItem.copy(startDate = vacationItem.startDate - 1)
//            updateItem(newItem)
//        }
    }

    /**
     * Inserts the new Item into database.
     */
    fun addNewItem(itemTitle: String, itemPlace: String, itemStartDate: String, itemEndDate: String) {
        val newItem = getNewItemEntry(itemTitle, itemPlace, itemStartDate, itemEndDate)
        insertItem(newItem)
    }

    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    private fun insertItem(vacationItem: VacationItem) {
        viewModelScope.launch {
            itemDao.insert(vacationItem)
        }
    }

    /**
     * Launching a new coroutine to delete an item in a non-blocking way
     */
    fun deleteItem(vacationItem: VacationItem) {
        viewModelScope.launch {
            itemDao.delete(vacationItem)
        }
    }

    /**
     * Retrieve an item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<VacationItem> {
        return itemDao.getItem(id).asLiveData()
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Returns an instance of the [VacationItem] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewItemEntry(itemTitle: String, itemPlace: String, itemStartDate: String, itemEndDate: String): VacationItem {
        return VacationItem(
            title = itemTitle,
            place = itemPlace,
            startDate = itemStartDate,
            endDate = itemEndDate
        )
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [VacationItem] entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemTitle: String,
        itemPlace: String,
        itemStartDate: String,
        itemEndDate: String
    ): VacationItem {
        return VacationItem(
            id = itemId,
            title = itemTitle,
            place = itemPlace,
            startDate = itemStartDate,
            endDate = itemEndDate
        )
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

