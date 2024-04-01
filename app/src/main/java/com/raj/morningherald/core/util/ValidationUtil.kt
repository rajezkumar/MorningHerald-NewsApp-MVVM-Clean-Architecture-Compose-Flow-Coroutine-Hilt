package com.raj.morningherald.core.util

object ValidationUtil {

    fun checkIfValidArgNews(str: String?): Boolean {
        return !(str.isNullOrEmpty() || str == "{source}")
    }

}