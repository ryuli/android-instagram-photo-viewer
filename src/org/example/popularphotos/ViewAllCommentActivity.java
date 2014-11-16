package org.example.popularphotos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

public class ViewAllCommentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_comment);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		ListView list = (ListView) findViewById(R.id.lvAllComment);
		Bundle extra = intent.getExtras();
		
		ArrayList<Comment> commentList = extra.getParcelableArrayList("comment_list");
		CommentAdapter adapter = new CommentAdapter(this, 0, commentList);
		list.setAdapter(adapter);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
