package org.example.popularphotos;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
	
	private User user;
	private String text;
	private long createTime;
	
	public Comment(JSONObject data) {
		JSONObject userInfo = new JSONObject();
		if (data != null) {
			createTime = Integer.valueOf(data.optString("created_time")) * 1000L;
			text = data.optString("text");
			JSONObject from = data.optJSONObject("from");
			if (from != null) {
				userInfo = from;
			}
		} else {
			createTime = 0;
			text = "";
		}
		
		user = new User(userInfo);
	}
	
	public User getUser() {
		return user;
	}
	
	public String getText() {
		return text;
	}
	
	public long getCreateTime() {
		return createTime;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(text);
		dest.writeLong(createTime);
		dest.writeValue(user);
	}
	
	public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {

		@Override
		public Comment createFromParcel(Parcel in) {
			return new Comment(in);
		}

		@Override
		public Comment[] newArray(int size) {
			return new Comment[size];
		}
	};
	
	private Comment(Parcel in) {
		text = in.readString();
		createTime = in.readLong();
		user = (User) in.readValue(User.class.getClassLoader());
	}
	
}
