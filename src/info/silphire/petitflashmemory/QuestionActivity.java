package info.silphire.petitflashmemory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 問題を出題していくアクティビティクラス
 * 
 * @author Yuki Mitsui <silphire@gmail.com>
 *
 */
public class QuestionActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		Intent intent = getIntent();
		if(intent == null) {
			Log.d(Constants.LOG_TAG, "Cannot obtain Intent object");
			this.finish();
			return;
		}
		
		ProblemSet problemSet = (ProblemSet)intent.getSerializableExtra("problemSet");
		if(problemSet == null) {
			Log.d(Constants.LOG_TAG, "Cannot obtain associated ProblemSet");
			this.finish();
			return;
		}
	}
	
	@Override
	public void onClick(View view) {
		// どのボタンを押して解答したかを取得する
	}
}
