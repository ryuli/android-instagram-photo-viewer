package org.example.popularphotos;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	private String name;
	private String profilePictureUrl;
	
	public User(JSONObject data) {
		name = data.optString("username");
		profilePictureUrl = data.optString("profile_picture");			
	}
	
	public String getName() {
		return name;
	}
	
	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(profilePictureUrl);
	}
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
	
	private User(Parcel in) {
		name = in.readString();
		profilePictureUrl = in.readString();
	}
}
