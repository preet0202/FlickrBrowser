package com.example.flickrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlickrJsonData.OnDataAvailable,
                                    RecyclerItemClickListener.OnRecyclerClickListener{

    private static final String TAG = "MainActivity";
    private FlickrRecycleViewAdapter mFlickrRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);

        RecyclerView recyclerView =  (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView , this));
        mFlickrRecyclerViewAdapter = new FlickrRecycleViewAdapter(this, new ArrayList<Photo>());
        recyclerView.setAdapter(mFlickrRecyclerViewAdapter);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume start");
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryResult = sharedPreferences.getString(FLICKR_QUERY,"");

        if(queryResult.length()>0) {
            GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData(this, "https://www.flickr.com/services/feeds/photos_public.gne", "en-us", true);
            //   getFlickrJsonData.executeOnSameThread("naruto");
            getFlickrJsonData.execute(queryResult);
        }
        Log.d(TAG, "onResume ends ");
    }

    @Override//tells the compiler that the method is overridden from the super class and tells it to check the correct number and type of parameter
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_search){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }
        Log.d(TAG, "onOptionsItemSelected() returned: returned");
        return super.onOptionsItemSelected(item);
    }
    @Override
    public  void onDataAvailable(List<Photo> data , DownloadStatus status){
        Log.d(TAG, "onDataAvailable: starts");
        if(status == DownloadStatus.OK){
            mFlickrRecyclerViewAdapter.loadNewData(data);
            //Log.d(TAG, "onDataAvailable: data is" + data);
        }else{
            //download or processing failed
            Log.e(TAG, "onDataAvailable failed with status " + status );
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: start");
        Toast.makeText(MainActivity.this, "Normal tap at position "+ position ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: start");
        //Toast.makeText(MainActivity.this, "long tap at position "+ position ,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER , mFlickrRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }
}
