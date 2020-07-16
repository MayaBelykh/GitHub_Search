package com.agnitt.githubsearch.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

val URL = { query: String -> "https://api.github.com/search/repositories?q=${query}" }
val inflater = { context: Context, id: Int, parent: ViewGroup ->
    LayoutInflater.from(context).inflate(id, parent, false)
}
val toast = { context: Context, text: String ->
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}
val openURL = { context: Context, url: String ->
    context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) })
}

infix fun TextView.set(text: String): Boolean {
    if (text != "null" && text != "0") {
        this.text = text
        return true
    }
    return false
}

