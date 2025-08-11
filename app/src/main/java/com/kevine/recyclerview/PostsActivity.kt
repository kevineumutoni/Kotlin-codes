package com.kevine.recyclerview

import android.os.Bundle
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

class PostsActivity : AppCompatActivity() {
    lateinit var rvPosts : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_posts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rvPosts)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        rvPosts = findViewById(R.id.rvPosts)
        rvPosts.layoutManager = LinearLayoutManager(this)

        fetchPosts()


    }
    fun fetchPosts(){
        val retrofit= ApiClient.buildApiClient(ApiInterface::class.java)
        val requests=  retrofit.getPosts()
        requests.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful){
                    //obtain and display posts
                    val posts = response.body()!!
                    val postsAdapter = PostsRvAdapter(posts)
                    rvPosts.adapter= postsAdapter

                }else{
                    Toast.makeText(baseContext, response.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }
        })


    }

}