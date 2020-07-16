package com.agnitt.githubsearch.data

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.agnitt.githubsearch.R
import com.agnitt.githubsearch.model.Item
import com.agnitt.githubsearch.utils.inflater
import com.agnitt.githubsearch.utils.set

class CustomAdapter(private var context: Context, var items: ArrayList<Item>) :
    RecyclerView.Adapter<CustomAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var card: CardView = itemView.findViewById(R.id.card)
        var repoName: TextView = itemView.findViewById(R.id.tw_if_repo_name)
        var readme: TextView = itemView.findViewById(R.id.tw_if_readme)
        var stargazers: TextView = itemView.findViewById(R.id.tw_if_stargazers)
        var starIcon: ImageView = itemView.findViewById(R.id.iv_if_star)
        var language: TextView = itemView.findViewById(R.id.tw_if_language)
        var license: TextView = itemView.findViewById(R.id.tw_if_license)
        var updated: TextView = itemView.findViewById(R.id.tw_if_updated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(inflater(context, R.layout.item_found, parent))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("LOG________LOG", "bind")
        items[position].apply {
            holder.repoName set repoName
            holder.readme set readme
            if (holder.stargazers set stargazers)
                holder.starIcon.setImageResource(android.R.drawable.btn_star_big_on)
            else
                holder.starIcon.setImageDrawable(null)
            holder.language set language
            holder.license set license
            holder.updated set updated
            holder.card.tag = link
        }
    }

    override fun getItemCount(): Int = items.size
}