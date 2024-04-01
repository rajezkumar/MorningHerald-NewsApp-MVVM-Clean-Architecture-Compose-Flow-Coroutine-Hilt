package com.raj.morningherald.core.common.connectivity

interface ConnectivityChecker {
    fun hasInternetConnection(): Boolean
}