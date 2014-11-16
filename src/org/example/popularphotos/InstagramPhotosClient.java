package org.example.popularphotos;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class InstagramPhotosClient {
	private static final String SERVICE_URL = "https://api.instagram.com/v1";
	private static final String POPULAR_PHOTOS_URI = "/media/popular";
	private static final String CLIENT_ID = "4132363de5694ae39c2894cdc4a4b038";
	private static final String PARAM_NAME_CLIENT_ID = "client_id";
	
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void getPopularPhotos(AsyncHttpResponseHandler handler) {
		String url = SERVICE_URL + POPULAR_PHOTOS_URI;
		RequestParams params = new RequestParams();
		params.put(PARAM_NAME_CLIENT_ID, CLIENT_ID);
		client.get(url, params, handler);
	}
}
