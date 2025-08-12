package com.kevine.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentsRvAdapter(var comments: List<Comment>) : RecyclerView.Adapter<CommentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_list_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.tvCommentName.text = comment.name
        holder.tvCommentEmail.text = comment.email
        holder.tvCommentBody.text = comment.body
    }
}

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvCommentName: TextView = itemView.findViewById(R.id.tvCommentName)
    val tvCommentEmail: TextView = itemView.findViewById(R.id.tvCommentEmail)
    val tvCommentBody: TextView = itemView.findViewById(R.id.tvCommentBody)
}