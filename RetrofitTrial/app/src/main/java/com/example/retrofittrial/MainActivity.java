package com.example.retrofittrial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.retrofittrial.api_interfaces.JsonPlaceHolderApi;
import com.example.retrofittrial.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult=findViewById(R.id.text_view_result);

        getPosts();
    }
    void createPost(){
        Post post=new Post("Rajesh",123,"Mock Body Data","avsfg");

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http:192.168.2.16:3008/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        Call<Post> call = jsonPlaceHolderApi.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                Post postResponse = response.body();
                String content="";
                content += "Code: " + response.code()+"\n";
                content += "username: " + post.getUsername()+"\n";
                content += "phone: " + post.getPhone()+"\n";
                content += "email: " + post.getEmail()+"\n";
                content += "password: " + post.getPassword()+"\n\n";

                textViewResult.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
            textViewResult.setText(t.getMessage());
            }

        });

    }

    public void getPosts(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3008/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Post>> call=jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Post> posts=response.body();
                for(Post post:posts){
                    String content="";
                    content += "username: " + post.getUsername()+"\n";
                    content += "phone: " + post.getPhone()+"\n";
                    content += "email: " + post.getEmail()+"\n";
                    content += "password: " + post.getPassword()+"\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}