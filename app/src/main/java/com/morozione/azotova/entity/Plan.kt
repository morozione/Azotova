package com.morozione.azotova.entity

class Plan {
    var id: String? = null
    var userId: String? = null
    var title: String? = null
    var description: String? = null
    var city: String? = null
    var user: List<String>? = null
    var date: Long = 0

    constructor() {}

    constructor(title: String, description: String,
                city: String, date: Long) {
        this.title = title
        this.description = description
        this.city = city
        this.date = date
    }
}
