package com.example.cpu02351_local.firebasechatapp


fun <E> java.util.ArrayList<E>.addOrUpdateAll(list: List<E>) {
    list.forEach {item ->
        this.addOrUpdate(item)
    }
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
    if (pos == -1) {
        this.add(item)
    } else {
        this[pos] = item
    }
}

