package org.example.popularphotos;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoAdapter extends ArrayAdapter<Photo> {

	private static final int NUM_OF_COMMENT_DISPLAYED = 2;
	private static final int COMMENT_TEXT_VIEW_PADDING_BOTTOM = 5;
	private static final int COMMENT_TEXT_VIEW_PADDING_RIGHT = 3;
	private Context context;
	private List<Photo> photos;

	public PhotoAdapter(Context context, List<Photo> photos) {
		super(context, 0, photos);
		this.context = context;
		this.photos = photos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.photo,
					parent, false);
		}

		ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
		ImageView ivUserImage = (ImageView) convertView
				.findViewById(R.id.ivUserImage);
		TextView tvUsername = (TextView) convertView
				.findViewById(R.id.tvUsername);
		TextView tvElapsedTime = (TextView) convertView
				.findViewById(R.id.tvElapsedTime);
		TextView tvLikeCount = (TextView) convertView
				.findViewById(R.id.tvLikeCount);
		LinearLayout layoutUserComment = (LinearLayout) convertView
				.findViewById(R.id.layoutUserComment);

		Photo photo = photos.get(position);

		String imgUrl = photo.getImageUrl();

		if (!imgUrl.equals("")) {
			Picasso.with(context).load(photo.getImageUrl())
					.placeholder(R.drawable.camera_gray).into(ivImage);
		}

		User user = photo.getUser();
		String userImgUrl = user.getProfilePictureUrl();

		if (!userImgUrl.equals("")) {
			Picasso.with(context).load(userImgUrl).into(ivUserImage);
		} else {
			ivUserImage.setImageResource(R.drawable.human2_gray);
		}

		long createTime = photo.getCreateTime();
		long now = System.currentTimeMillis();
		CharSequence timeElapsedDesc = DateUtils.getRelativeTimeSpanString(
				createTime, now, DateUtils.SECOND_IN_MILLIS);
		tvUsername.setText(user.getName());

		tvElapsedTime.setText(timeElapsedDesc);
		tvLikeCount.setText(String.valueOf(photo.getLikeCount()));

		layoutUserComment.removeAllViews();
		TextView tvOwnerComment = new TextView(context);
		tvOwnerComment.setPadding(0, 0, COMMENT_TEXT_VIEW_PADDING_RIGHT,
				COMMENT_TEXT_VIEW_PADDING_BOTTOM);
		tvOwnerComment.setText(getUserCommentHtml(user.getName(),
				photo.getCaption()));
		layoutUserComment.addView(tvOwnerComment);

		int commentCount = photo.getCommentCount();
		if (commentCount > 0) {

			if (commentCount > NUM_OF_COMMENT_DISPLAYED) {
				TextView tvViewAllComment = new TextView(context);
				tvViewAllComment.setId(position);
				tvViewAllComment.setPadding(0, 0,
						COMMENT_TEXT_VIEW_PADDING_RIGHT,
						COMMENT_TEXT_VIEW_PADDING_BOTTOM);
				tvViewAllComment.setTypeface(null, Typeface.BOLD);
				tvViewAllComment.setText(getViewAllCommentHint(commentCount));
				tvViewAllComment.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TextView tv = (TextView) v;
						int id = tv.getId();
						
						Photo photo = PhotoAdapter.this.photos.get(id);
						Intent intent = new Intent(context, ViewAllCommentActivity.class);
						Bundle extra = new Bundle();
						extra.putParcelableArrayList("comment_list", photo.getCommentList());
						intent.putExtras(extra);
						
						context.startActivity(intent);
					}
				});
				layoutUserComment.addView(tvViewAllComment);

			}

			ArrayList<Comment> commentList = photo.getCommentList();
			for (int i = 0; i < commentList.size(); i++) {
				if ((i + 1) > NUM_OF_COMMENT_DISPLAYED) {
					break;
				}

				Comment comment = commentList.get(i);
				String username = comment.getUser().getName();
				String text = comment.getText();

				TextView tvUserComment = new TextView(context);
				tvUserComment.setPadding(0, 0, COMMENT_TEXT_VIEW_PADDING_RIGHT,
						COMMENT_TEXT_VIEW_PADDING_BOTTOM);
				tvUserComment.setText(getUserCommentHtml(username, text));
				layoutUserComment.addView(tvUserComment);

			}
		}

		return convertView;
	}

	private String getViewAllCommentHint(int commentCount) {
		String viewAllComment = context.getString(R.string.view_all_of) + " "
				+ String.valueOf(commentCount) + " "
				+ context.getString(R.string.comments);

		return viewAllComment;
	}

	private Spanned getUserCommentHtml(String username, String commentText) {
		Spanned html = Html.fromHtml("<font color='#0174DF'>" + username + "</font> "
				+ commentText);

		return html;
	}

}
