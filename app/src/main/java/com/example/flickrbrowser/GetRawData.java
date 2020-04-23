package com.example.flickrbrowser;

//class to download data from flikr feed

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Struct;

enum DownloadStatus {IDLE , PROCESSING , NOT_INITIALISED , FAILED_OR_EMPTY , OK }//tells abut the status of data
//this class is used to download data that can run on async task(background thread) by calling the execute method
// and can also run on the main thread when it calls the runinSameThread for quick download and
//sent it to on_download_method
class GetRawData extends AsyncTask<String ,Void ,String> {
    private static final String TAG="GetRawData";
    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallback;

    interface  OnDownloadComplete{
        void onDownloadComplete(String data, DownloadStatus status);
    }


    public GetRawData(OnDownloadComplete callback) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        mCallback = callback;
    }

    void runInSameThread(String s){
        Log.d(TAG, "runInSameThread: starts");

      //  onPostExecute(doInBackground(s));
        if(mCallback != null){
          //  String result = doInBackground(s);
           // mCallback.onDownloadComplete(result, mDownloadStatus);
            mCallback.onDownloadComplete(doInBackground(s), mDownloadStatus);
        }

        Log.d(TAG, "runInSameThread: ends");
    }

    @Override//when we call the execute the method of Async class it create the new thread and runs the doinbackground
    // on background thread and then call onpostexecute on the main thread
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter =" + s);
        if(mCallback != null){
            mCallback.onDownloadComplete(s, mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute ends");
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if(strings == null){
            mDownloadStatus= DownloadStatus.NOT_INITIALISED;
            return null;
        }
        try{
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();;
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground:  the response code was " +response);

            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

//            String line;
//            while(null != (line = reader.readLine())){//reason for null becoz first the line readers the data then checks for null
            for(String line = reader.readLine();line!=null ;line = reader.readLine()){
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            //now the finally block is executed and then the return statement is executed
            return result.toString();

        }catch(MalformedURLException e){
            Log.e(TAG, "doInBackground: Invalid URL "+ e.getMessage() );
        }catch(IOException e){
            Log.e(TAG, "doInBackground: IO Exception reading data" +e.getMessage());
        }catch(SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception, Need permission?"+ e.getMessage());//logd  will not be present once the app is deployed but loge are present in the logfile for tracking
        }finally{
            if(connection != null){
                connection.disconnect();
            }
            if(reader !=null){
                try{
                    reader.close();
                }catch(IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream"+ e.getMessage() );
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
