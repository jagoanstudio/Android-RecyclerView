package com.jagoanstudio.app.utils.view

object ViewText {

    fun splitFullname(fullname: String?): String {
        var newFullname = fullname
        if (newFullname?.contains(" ") == true) {
            val listFullname = newFullname.split(" ").toTypedArray()
            var name = ""
            for (i in listFullname) {
                if (i.isNotEmpty()) {
                    name += i.plus(" ")
                }
            }
            newFullname = name.split(' ')[0].first().toUpperCase().plus("").plus(name.split(' ')[1].first().toUpperCase())
        } else {
            if (newFullname != null) {
                newFullname = newFullname.take(2).toUpperCase()
            }
        }

        return newFullname.toString()
    }

}