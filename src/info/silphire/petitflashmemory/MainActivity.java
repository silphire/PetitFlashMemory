package info.silphire.petitflashmemory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 
 * ���̑I����ʂł��B
 * 
 * @author Yuki Mitsui <silphire@gmail.com>
 *
 */
public class MainActivity extends ActionBarActivity {
	/**
	 * �ݒ肳��Ă���f�B���N�g��������̈ꗗ���擾���A�Ԃ��܂��B
	 * 
	 * @return�@�ǂݍ��񂾖��̈ꗗ
	 */
	public List<ProblemSet> GetProblems() {
		List<ProblemSet> listProblemSet = new ArrayList<ProblemSet>();
		
		// ��肪���݂���f�B���N�g�����擾����
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String problemPath = prefs.getString("problemPath", null);
		Log.d(Constants.LOG_TAG, "problemPath: " + problemPath);
		
		if(problemPath == null) {
			// Preference����K��̃f�B���N�g���̐ݒ肪�ǂݍ��߂Ȃ��ꍇ
			for(String file : this.fileList()) {
				ProblemSet problemSet = readOneProblemSet(new File(file));
				if(problemSet != null) {
					listProblemSet.add(problemSet);
				}
			}
		} else {
			// �K��̃f�B���N�g���̏ꏊ�����������ꍇ
			File problemDirObj = new File(problemPath);
			for(File f : problemDirObj.listFiles()) {
				ProblemSet problemSet = readOneProblemSet(f);
				if(problemSet != null) {
					listProblemSet.add(problemSet);
				}
			}
		}
		
		return listProblemSet;
	}
	
	protected ProblemSet readOneProblemSet(File file) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
		} catch(FileNotFoundException e) {
			// �t�@�C���������ꍇ�͒P�ɖ������Ď�����������
			Log.d(Constants.LOG_TAG, "File not found -- " + file.getPath());
			return null;
		}
		
		ProblemSet problemSet = new ProblemSet();
		try {
			problemSet.parseMetadata(stream);
		} catch (IllegalFormatException | SAXException | IOException
				| ParserConfigurationException | ParseException e) {
			// parse�o���Ȃ��̂Ȃ�΁A�������Ď��ɍs�������B
			Log.d(Constants.LOG_TAG, "Failed to parse problem set -- " + file.getParent());
		}
		
		try {
			stream.close();
			return problemSet;
		} catch (IOException e) {
			// close�o���Ȃ������Ȃ�Ƃ肠��������
			return null;
		}
	}
	
	/**
	 * �ˋ�̖���\�����܂� (�e�X�g�p)
	 */
	protected ProblemSet GetSampleProblems() {
		ProblemSet problemSet = new ProblemSet();
		
		problemSet.setTitle("*** SAMPLE ***");
		problemSet.setCreator("John Doe");
		problemSet.setCreatedDate(new Date());
		problemSet.setProblemList(new ArrayList<Problem>());
		
		return problemSet;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO implement ���ꗗ���擾����metadata����ʂɃ��X�g�r���[�ŕ\������
		ProblemSet problemSet = GetSampleProblems();
		ProblemMetadataView view = new ProblemMetadataView(problemSet, this, null);
		setContentView(view);
		
		// setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
