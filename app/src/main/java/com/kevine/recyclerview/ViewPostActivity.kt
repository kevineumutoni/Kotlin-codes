package com.kevine.recyclerview

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewPostActivity : AppCompatActivity() {
    var postId = 0
    lateinit var commentsAdapter: CommentsRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (intent.extras != null) {
            postId = intent.extras!!.getInt("POST_ID")
        }

        // Setup RecyclerView
        val rvComments = findViewById<RecyclerView>(R.id.rvComments)
        commentsAdapter = CommentsRvAdapter(listOf())
        rvComments.adapter = commentsAdapter
        rvComments.layoutManager = LinearLayoutManager(this)

        fetchPostDetails()
        fetchComments()
    }

    private fun fetchPostDetails() {
        val apiClient = ApiClient.buildApiClient(ApiInterface::class.java)
        apiClient.getPostById(postId).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    val post = response.body()
                    findViewById<TextView>(R.id.tvPostTitle).text = post?.title
                    findViewById<TextView>(R.id.tvPostBody).text = post?.body
                    findViewById<TextView>(R.id.tvPostUserId).text = "User: ${post?.userId}"
                } else {
                    Toast.makeText(this@ViewPostActivity, "Failed to load post details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Toast.makeText(this@ViewPostActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchComments() {
        val apiClient = ApiClient.buildApiClient(ApiInterface::class.java)
        apiClient.getCommentsByPostId(postId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    val comments = response.body() ?: listOf()
                    commentsAdapter.comments = comments
                    commentsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@ViewPostActivity, "Failed to load comments", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Toast.makeText(this@ViewPostActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}