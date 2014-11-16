package org.example.popularphotos;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class PopularPhotosActivity extends Activity {

	private SwipeRefreshLayout swipeContainer;
	private ProgressDialog loadDialog;
	private AlertDialog.Builder alertDialog;
	private ArrayList<Photo> photos;
	private ListView lvPhoto;
	private PhotoAdapter photoAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popular_photos);

		lvPhoto = (ListView) findViewById(R.id.lvPhoto);
		photos = new ArrayList<Photo>();
		initSwipeRefresh();
		initDialog();
		InstagramPhotosClient.getPopularPhotos(new InstagramResponseHandler());
	}
	
	private void initSwipeRefresh() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
        		android.R.color.holo_green_light,
        		android.R.color.holo_orange_light,
        		android.R.color.holo_red_light);
        
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				InstagramPhotosClient.getPopularPhotos(new InstagramResponseHandler());
			}
		});
	}
	
	private void initDialog() {
		loadDialog = new ProgressDialog(PopularPhotosActivity.this);

		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle(getString(R.string.alert_dialog_title))
				.setPositiveButton(getString(R.string.btn_lable_ok),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
	}

	private void showProcessDialog() {
		loadDialog.setMessage(getString(R.string.load_data_hint));
		loadDialog.show();
	}

	private void showAlertDialog(String message) {
		if (loadDialog.isShowing()) {
			loadDialog.dismiss();
		}
		alertDialog.setMessage(message).show();
	}

	private void preparePhotoList() {
		photoAdapter = new PhotoAdapter(this, photos);
		lvPhoto.setAdapter(photoAdapter);
	}

	private class InstagramResponseHandler extends JsonHttpResponseHandler {

		private static final int STATUS_OK = 200;

		@Override
		public void onStart() {
			showProcessDialog();
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {

			try {
				JSONObject meta = response.getJSONObject("meta");
				int code = meta.getInt("code");
				if (code != STATUS_OK) {
					showAlertDialog(getString(R.string.fail_to_load_photo));
					return;
				}
				
				photos.clear();

				JSONArray data = response.getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					JSONObject info = data.getJSONObject(i);
					Photo photo = new Photo(info);
					photos.add(photo);
				}
				
				if (swipeContainer.isRefreshing()) {
					photoAdapter.notifyDataSetChanged();
				} else {
					preparePhotoList();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				showAlertDialog(getString(R.string.fail_to_load_photo));
			}

			loadDialog.dismiss();
			if (swipeContainer.isRefreshing()) {
				swipeContainer.setRefreshing(false);
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			showAlertDialog(getString(R.string.fail_to_load_photo));
			if (swipeContainer.isRefreshing()) {
				swipeContainer.setRefreshing(false);
			}
		}

	}
	
}
