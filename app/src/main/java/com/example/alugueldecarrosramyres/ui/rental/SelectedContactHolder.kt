package com.example.alugueldecarrosramyres.ui.rental

object SelectedContactHolder {
    var contactId: String? = null
    var name: String? = null
    var phone: String? = null

    fun set(id: String, n: String, p: String) {
        contactId = id
        name = n
        phone = p
    }

    fun clear() {
        contactId = null
        name = null
        phone = null
    }
}
