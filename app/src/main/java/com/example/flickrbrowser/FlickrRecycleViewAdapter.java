package com.example.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecycleViewAdapter extends RecyclerView.Adapter<FlickrRecycleViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecycleViewAdapte";
    private List<Photo> mPhotoList;
    private Context mContext;

    public FlickrRecycleViewAdapter(Context context , List<Photo> photoList) {
        mPhotoList = photoList;
        mContext = context;
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ///called by the layout manager when it when it needs a new view
        Log.d(TAG, "onCreateViewHolder:  new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse , parent , false);
        return new FlickrImageViewHolder(view);
    }

    @Override//it is called by the recycler view when it wants new data to be stored  in a view holder so that it can display it
    //this can be done also by storing the deatils of photo but in this case we are only storing the url
    //we can use a dependency called piccasso which does the same work and store the photo in the cache whih speeds up the process
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
            //called by the layout manager when it new data in a existing re
        if(mPhotoList == null || mPhotoList.size()==0){
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText(R.string.empty_photo);
        }else{
            Photo photoItem = mPhotoList.get(position);
            Log.d(TAG, "onBindViewHolder: "+ photoItem.getTitle() + "-->" + position );
            Picasso.with(mContext).load(photoItem.getImage())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnail);

            holder.title.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
      //  Log.d(TAG, "getItemCount: called");
        //returning 1 beacuse telling the compiler to display no photo available else it would print nothing
        return ((mPhotoList !=null) && (mPhotoList.size() != 0)? mPhotoList.size() : 1);
    }

    void loadNewData(List <Photo> newPhoto){
        mPhotoList = newPhoto;
        notifyDataSetChanged();
    }
    public Photo getPhoto(int position){
        return ((mPhotoList !=null) && (mPhotoList.size() != 0)? mPhotoList.get(position) : null);
    }
    //by making it static it behaves as another top level class i9n another file
    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(View itemView){
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: start");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.photo_title);
        }
    }
}
