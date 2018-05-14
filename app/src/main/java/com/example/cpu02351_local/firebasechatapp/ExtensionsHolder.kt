package com.example.cpu02351_local.firebasechatapp

import android.util.Log


fun <E> java.util.ArrayList<E>.addOrUpdateAll(list: List<E>) {
    list.forEach {item ->
        this.addOrUpdate(item)
    }
    Log.d("DEBUGGING_EXT", "${this.size}")
}

fun <E> ArrayList<E>.addIfNotContains(obs: E) {
    if (!this.contains(obs)) {
        this.add(obs)
    }
}

fun <E> ArrayList<E>.removeIfContains(obs: E) {
    this.remove(obs)
}

fun <E> java.util.ArrayList<E>.addOrUpdate(item: E) {
    val pos = this.indexOf(item)
    Log.d("DEBUGGING", "Index: $pos")
    if (pos == -1) {
        Log.d("DEBUGGING", "Item added")
        this.add(item)
    } else {
        this[pos] = item
    }
}

