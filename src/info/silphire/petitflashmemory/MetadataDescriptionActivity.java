package info.silphire.petitflashmemory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 
 * ���ꗗ��ʂŖ��𒷉����I�����ꂽ���ɁA���̖��̃��^�f�[�^��\������ׂ̃A�N�e�B�r�e�B
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
