package com.agnitt.githubsearch.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agnitt.githubsearch.R
import com.agnitt.githubsearch.data.CustomAdapter
import com.agnitt.githubsearch.model.Item
import com.agnitt.githubsearch.utils.URL
import com.agnitt.githubsearch.utils.openURL
import com.agnitt.githubsearch.utils.toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CustomAdapter
    lateinit var items: ArrayList<Item>
    lateinit var requestQueue: RequestQueue

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = rv_found
        recyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        items = arrayListOf()
        requestQueue = Volley.newRequestQueue(this)

        getSubject().debounce(1, TimeUnit.SECONDS)
            .distinctUntilChanged()
            .filter { text -> text.isNotEmpty() }
            .subscribe { str -> getItems(URL(str)) }
    }

    private fun getSubject(): PublishSubject<String> {
        var subject: PublishSubject<String> = PublishSubject.create()
        sv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = subject.onNext(query!!) == null
            override fun onQueryTextChange(newText: String?) = subject.onNext(newText!!) == null
        })
        return subject
    }

    private fun getItems(url: String) {
        items.clear()
        var request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                var jsonArr = response.getJSONArray("items")
                if (jsonArr.length() == 0)
                    toast(this, getText(R.string.empty_result).toString())

                for (i in 0 until jsonArr.length()) {
                    var jsonObject = jsonArr.getJSONObject(i)
                    var get = { parameter: String -> jsonObject[parameter].toString() }
                    var getInner = { parameter: String, name: String ->
                        if (jsonObject[parameter].toString() != "null")
                            jsonObject.getJSONObject(parameter)[name].toString()
                        else "null"
                    }

                    items.add(
                        Item(
                            repoName = get("full_name"),
                            readme = get("description"),
                            stargazers = get("stargazers_count"),
                            language = get("language"),
                            license = getInner("license", "spdx_id"),
                            updated = get("updated_at").substring(0, 10),
                            link = get("html_url")
                        )
                    )
                }
                adapter = CustomAdapter(this@MainActivity, items)
                recyclerView.adapter = adapter
            }, Response.ErrorListener {
                toast(this, getText(R.string.error_listener).toString())
            })
        requestQueue.add(request)

    }

    fun openRepo(v: View) = openURL(this, v.tag.toString())
}