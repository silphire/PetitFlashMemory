package info.silphire.petitflashmemory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
		
		setContentView(R.layout.activity_metadata_description);
		
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
		
		TextView title = (TextView) findViewById(R.id.metadeta_desc_title);
		title.setText(problemSet.getTitle());
		
		TextView creator = (TextView) findViewById(R.id.metadeta_desc_creator);
		creator.setText(problemSet.getCreator());
		
		TextView createdDate = (TextView) findViewById(R.id.metadeta_desc_created_date);
		createdDate.setText(problemSet.getCreatedDate().toString());	// TODO きちんとフォーマットを整える
	}
}
