package com.phonegap.plugins.downloader;

import android.util.Log;
import android.app.DownloadManager;
import android.net.Uri;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Downloader extends Plugin {

	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
	    	
		if (!action.equals("downloadFile")) 
			return new PluginResult(PluginResult.Status.INVALID_ACTION);
		
		try {
			JSONObject params = args.getJSONObject(0);

			String fileUrl = params.getString("fileUrl");
			String dirName = params.getString("dirName");
			String fileName = params.has("fileName") ? 
					params.getString("fileName") : fileUrl.substring(fileUrl.lastIndexOf("/")+1);
			
			return this.downloadUrl(fileUrl, dirName, fileName);
			
		} catch (JSONException e) {
            String err = e.getMessage();
            Log.e("CordovaDM", err);
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION, err);
		} catch (Exception e) {
            String err = e.getMessage();
            Log.e("CordovaDM", err);
			return new PluginResult(PluginResult.Status.ERROR, err);
		}

	}

	private PluginResult downloadUrl(String fileUrl, String dirName, String fileName ) {
        File folder = this.createDirs("/sdcard/", dirName);
        File file   = new File(folder, fileName);
        Log.d("CordovaDM", file.toString());
		if( !file.exists() ){
			DownloadManager.Request r = 
                new DownloadManager.Request( Uri.parse(Uri.encode(fileUrl, "/:")) )
                                    //.setDestinationUri( Uri.fromFile(folder) )
                                    .setDestinationInExternalPublicDir(dirName, fileName)
			                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
			r.allowScanningByMediaScanner();

			DownloadManager dm = (DownloadManager) cordova.getActivity()
				.getSystemService(android.content.Context.DOWNLOAD_SERVICE);
			dm.enqueue(r);
		}
		return new PluginResult(PluginResult.Status.OK, "yeah");
	}

    private File createDirs(String root, String dirPath ){
        String current = root + "/" + dirPath;
        File f = new File(current);
        f.mkdirs();
        return f;
    }

    /*
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra( DownloadManager.EXTRA_DOWNLOAD_ID, 0 );
                Query query = new Query();
                query.setFilterById(enqueue);
                Cursor c = dm.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                        ImageView view = (ImageView) findViewById(R.id.imageView1);
                        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        view.setImageURI(Uri.parse(uriString));
                    }
                }
            }
        }
    };

    registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    */
}
