// File: app/src/main/java/com/example.brewspotin/presentation/viewmodel/MenuViewModel.kt
package com.example.brewspotin.ui.screen.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewspotin.data.model.MenuItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.FirebaseException

class MenuViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage

    init {
        loadMenuItems()
    }

    fun loadMenuItems() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("Cafe")
                    .document("Jokopi")
                    .collection("menu")
                    .get()
                    .await()

                val items = snapshot.documents.mapNotNull { doc ->
                    val menuItem = doc.toObject(MenuItem::class.java)
                    menuItem
                }
                _menuItems.value = items
                Log.d("MenuViewModel", "Menu items loaded: ${items.size}")
            } catch (e: FirebaseException) {
                _uiMessage.value = "Error memuat menu: ${e.message}"
                Log.e("MenuViewModel", "Error loading menu items from Firestore: ${e.message}", e)
            } catch (e: Exception) {
                _uiMessage.value = "Terjadi kesalahan: ${e.localizedMessage}"
                Log.e("MenuViewModel", "Unexpected error loading menu items: ${e.localizedMessage}", e)
            }
        }
    }

    fun addMenuItem(menuItem: MenuItem, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                firestore.collection("Cafe")
                    .document("Jokopi")
                    .collection("menu")
                    .add(menuItem) // add() akan membuat ID dokumen otomatis
                    .await()
                onComplete(true, null)
                _uiMessage.value = "Menu berhasil ditambahkan!"
                loadMenuItems()
                Log.d("MenuViewModel", "Menu item added: ${menuItem.name}")
            } catch (e: FirebaseException) {
                val errorMessage = "Gagal menambahkan menu: ${e.message}"
                onComplete(false, errorMessage)
                _uiMessage.value = errorMessage
                Log.e("MenuViewModel", "Error adding menu item to Firestore: ${e.message}", e)
            } catch (e: Exception) {
                val errorMessage = "Terjadi kesalahan saat menambahkan menu: ${e.localizedMessage}"
                onComplete(false, errorMessage)
                _uiMessage.value = errorMessage
                Log.e("MenuViewModel", "Unexpected error adding menu item: ${e.localizedMessage}", e)
            }
        }
    }

    fun deleteMenuItem(menuId: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                firestore.collection("Cafe")
                    .document("Jokopi")
                    .collection("menu")
                    .document(menuId)
                    .delete()
                    .await()
                onComplete(true, null)
                _uiMessage.value = "Menu berhasil dihapus!"
                loadMenuItems()
                Log.d("MenuViewModel", "Menu item deleted: $menuId")
            } catch (e: FirebaseException) {
                val errorMessage = "Gagal menghapus menu: ${e.message}"
                onComplete(false, errorMessage)
                _uiMessage.value = errorMessage
                Log.e("MenuViewModel", "Error deleting menu item from Firestore: ${e.message}", e)
            } catch (e: Exception) {
                val errorMessage = "Terjadi kesalahan saat menghapus menu: ${e.localizedMessage}"
                onComplete(false, errorMessage)
                _uiMessage.value = errorMessage
                Log.e("MenuViewModel", "Unexpected error deleting menu item: ${e.localizedMessage}", e)
            }
        }
    }

    fun clearUiMessage() {
        _uiMessage.value = null
    }
}
