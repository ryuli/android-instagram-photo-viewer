package org.example.popularphotos;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
	private String caption;
	private String imageUrl;
	private User user;
	private long createTime;
	private int likeCount;
	private int commentCount;
	private JSONObject data;
	private ArrayList<Comment> commentList;
	
	public Photo(JSONObject data) throws JSONException {
		this.data = data;
		commentList = new ArrayList<Comment>();
		parseCaption();
		parseImageUrl();
		parseUser();
		pareseCreateTime();
		parseLikes();
		parseComments();
	}
	
	public User getUser() {
		return user;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public long getCreateTime() {
		return createTime;
	}
	
	public int getLikeCount() {
		return likeCount;
	}
	
	public int getCommentCount() {
		return commentCount;
	}
	
	public ArrayList<Comment> getCommentList() {
		return commentList;
	}
	
	private void parseCaption() throws JSONException {
		JSONObject info = data.optJSONObject("caption");
		caption = (info != null) ? info.optString("text") : "" ;
	}
	
	private void parseImageUrl() throws JSONException {
		JSONObject info = data.optJSONObject("images");
		if (info != null) {
			JSONObject standard = info.optJSONObject("standard_resolution");
			imageUrl = (standard != null) ? standard.optString("url") : "" ;			
		} else {
			imageUrl = "";
		}
	}
	
	private void parseUser() throws JSONException {
		JSONObject info = data.optJSONObject("user");
		user = new User(info);
	}
	
	private void pareseCreateTime() {
		String time = data.optString("created_time");
		createTime = (!time.equals("")) ? Integer.valueOf(time) * 1000L : 0 ;
	}
	
	private void parseLikes() {
		JSONObject like = data.optJSONObject("likes");
		if (like != null) {
			likeCount = like.optInt("count");
		} else {
			likeCount = 0;
		}
	}
	
	private void parseComments() throws JSONException {
		JSONObject comments = data.optJSONObject("comments");
		if (comments != null) {
			commentCount = comments.optInt("count");
			JSONArray commentData = comments.optJSONArray("data");
			if (commentData != null) {
				for (int i = 0; i < commentData.length(); i++) {
					JSONObject info = commentData.optJSONObject(i);
					if (info != null) {
						Comment comment = new Comment(info);
						commentList.add(comment);
						
					}
				}
			}
		} else {
			commentCount = 0;
		}
	}
	
}
