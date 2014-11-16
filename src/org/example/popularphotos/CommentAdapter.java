package org.example.popularphotos;

import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends ArrayAdapter<Comment> {
	
	private Context context;
	private List<Comment> comments;

	public CommentAdapter(Context context, int resource, List<Comment> comments) {
		super(context, 0, comments);
		this.context = context;
		this.comments = comments;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.comment, parent, false);
		}
		
		TextView tvCommentUsername = (TextView) convertView.findViewById(R.id.tvCommentUsername);
		TextView tvUserComment = (TextView) convertView.findViewById(R.id.tvUserComment);
		ImageView ivCommentUserImage = (ImageView) convertView.findViewById(R.id.ivCommentUserImage);
		TextView tvCommentElapsedTime = (TextView) convertView.findViewById(R.id.tvCommentElapsedTime);
		
		Comment comment = comments.get(position);
		String text = comment.getText();
		User user = comment.getUser();
		
		tvCommentUsername.setText(user.getName());
		tvUserComment.setText(text);
		
		String userImageUrl = user.getProfilePictureUrl();
		if (!userImageUrl.equals("")) {
			Picasso.with(context).load(userImageUrl).into(ivCommentUserImage);
		} else {
			ivCommentUserImage.setImageResource(R.drawable.human2_gray);
		}
		
		CharSequence timeElapsedDesc = DateUtils.getRelativeTimeSpanString(
				comment.getCreateTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		tvCommentElapsedTime.setText(timeElapsedDesc);
		
		return convertView;
	}

}
