package info.silphire.petitflashmemory;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 問題を出題していくアクティビティクラス
 * 
 * @author Yuki Mitsui <silphire@gmail.com>
 *
 */
public class QuestionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
	}
}
