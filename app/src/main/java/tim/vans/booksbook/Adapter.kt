package tim.vans.booksbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tim.vans.booksbook.data.Book
import kotlin.math.log

class Adapter(private val dataSet: List <Book>):
    RecyclerView.Adapter<Adapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.bookTitle)
        val authorText: TextView = view.findViewById(R.id.bookAuthor)
        val opinionText: TextView = view.findViewById(R.id.bookOpinion)
        val scoreInt: TextView = view.findViewById(R.id.bookScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleText.text = dataSet[position].title
        holder.authorText.text = dataSet[position].author
        holder.opinionText.text = dataSet[position].opinion
        var scoreString:String = dataSet[position].score.toString()
        scoreString += "/5"
        holder.scoreInt.text = scoreString
    }

    override fun getItemCount() = dataSet.size

}