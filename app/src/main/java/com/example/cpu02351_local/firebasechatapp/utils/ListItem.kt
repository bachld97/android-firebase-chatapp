package com.example.cpu02351_local.firebasechatapp.utils

interface ListItem {
    fun equalsItem(otherItem: ListItem): Boolean
    fun equalsContent(otherItem: ListItem): Boolean
}