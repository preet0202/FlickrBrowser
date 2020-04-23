package com.example.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecyclerItemClickListen";

    interface OnRecyclerClickListener {
        void onItemClick(View view , int position);
        void onItemLongClick(View view ,int position);
    }

    private final OnRecyclerClickListener mListener;
    private final GestureDetectorCompat mGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnRecyclerClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts");
                View childview = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childview !=null && mListener !=null){
                    Log.d(TAG, "onSingleTapUp: calling listener.OnitemClick");
                    mListener.onItemClick(childview , recyclerView.getChildAdapterPosition(childview));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: starts");
                View childview = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childview !=null && mListener !=null){
                    Log.d(TAG, "onLongPress: calling listener.OnitemClick");
                    mListener.onItemLongClick(childview , recyclerView.getChildAdapterPosition(childview));
                }
            }
        });
    }

    @Override//called when any kind of action happens ie touch tab double tap or scrolling
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: start");
        if(mGestureDetector != null){
            boolean result = mGestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent() returned :" + result);
            return result;
        }else{
            Log.d(TAG, "onInterceptTouchEvent: returned : false");
            return false;
        }
    }
}
