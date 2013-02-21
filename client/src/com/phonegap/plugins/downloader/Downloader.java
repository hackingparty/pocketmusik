package com.phonegap.plugins.downloader;

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
			e.printStackTrace();
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION, e.getMessage());
		}
	}

	private PluginResult downloadUrl(String fileUrl, String dirName, String fi             leName ) {
		if( !new File(String.format("/sdcard%s/%s", dirName, fileName)).exists() ){
			DownloadManager.Request r = new DownloadManager.Request( Uri.parse(Uri.encode(fileUrl, "/:")) );

			r. setDestinationInExternalPublicDir( dirName, fileName );
			r.allowScanningByMediaScanner();
			r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

			DownloadManager dm = (DownloadManager) cordova.getActivity()
				.getSystemService(android.content.Context.DOWNLOAD_SERVICE);
			dm.enqueue(r);
		}
		return new PluginResult(PluginResult.Status.OK, "yeah");
	}
}
