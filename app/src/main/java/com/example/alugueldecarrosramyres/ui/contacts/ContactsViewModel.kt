package com.example.alugueldecarrosramyres.ui.contacts

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ContactItem(
    val contactId: String,
    val name: String,
    val phone: String
)

class ContactsViewModel(application: Application) : AndroidViewModel(application) {
    private val _contacts = MutableStateFlow<List<ContactItem>>(emptyList())
    val contacts: StateFlow<List<ContactItem>> = _contacts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadContacts() {
        viewModelScope.launch {
            _isLoading.value = true
            val list = withContext(Dispatchers.IO) {
                val fetched = mutableListOf<ContactItem>()
                val context = getApplication<Application>().applicationContext
                val cursor = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    ),
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
                )

                cursor?.use {
                    val idIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                    val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val numberIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    while (it.moveToNext()) {
                        val contactId = if (idIdx != -1) it.getString(idIdx) ?: "" else ""
                        val name = if (nameIdx != -1) it.getString(nameIdx) ?: "Desconhecido" else "Desconhecido"
                        val phone = if (numberIdx != -1) it.getString(numberIdx) ?: "" else ""
                        if (phone.isNotBlank()) {
                            fetched.add(ContactItem(contactId, name, phone))
                        }
                    }
                }
                fetched.distinctBy { it.phone }
            }
            _contacts.value = list
            _isLoading.value = false
        }
    }
}
