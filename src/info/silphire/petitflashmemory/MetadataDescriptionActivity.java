package info.silphire.petitflashmemory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 
 * 問題一覧画面で問題を長押し選択された時に、その問題のメタデータを表示する為のアクティビティ
 * 
 * @author Yuki Mitsui <silphire@gmail.com>
 *
 */
public class MetadataDescriptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		ProblemSet problemSet = (ProblemSet)intent.getSerializableExtra("problemSet");
		
		setContentView(R.layout.activity_metadata_description);
	}
}
