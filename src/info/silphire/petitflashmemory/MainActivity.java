package info.silphire.petitflashmemory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

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
	
	/**
	 * 
	 * �t�@�C���������ǂݍ���ŕԂ��܂�
	 * 
	 * @param file
	 * @return
	 */
	protected ProblemSet readOneProblemSet(File file) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
		} catch(FileNotFoundException e) {
			// �t�@�C���������ꍇ�͒P�ɖ������Ď�����������
			Log.d(Constants.LOG_TAG, e.getLocalizedMessage());
			return null;
		}
		
		ProblemSet problemSet = new ProblemSet();
		try {
			problemSet.parseMetadata(stream);
		} catch (IllegalFormatException | SAXException | IOException
				| ParserConfigurationException | ParseException e) {
			// parse�o���Ȃ��̂Ȃ�΁A�������Ď��ɍs�������B
			Log.d(Constants.LOG_TAG, e.getLocalizedMessage());
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
	protected List<ProblemSet> GetSampleProblems() {
		List<ProblemSet> listOfProblemSet = new ArrayList<ProblemSet>();
		ProblemSet problemSet;

		for(int i = 0; i < 20; ++i) {
			problemSet = new ProblemSet();
			problemSet.setTitle("*** SAMPLE " + i + " ***");
			problemSet.setCreator("John Doe " + i);
			problemSet.setCreatedDate(new Date());
			problemSet.setProblemList(new ArrayList<Problem>());
			listOfProblemSet.add(problemSet);
		}
		
		problemSet = new ProblemSet();
		problemSet.setTitle("�e�X�g");
		problemSet.setCreator("�������̌����q");
		problemSet.setCreatedDate(new Date());
		problemSet.setProblemList(new ArrayList<Problem>());
		listOfProblemSet.add(problemSet);
		
		return listOfProblemSet;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// List<ProblemSet> listOfProblemSet = GetSampleProblems();
		List<ProblemSet> listOfProblemSet = GetProblems();
		if(listOfProblemSet == null || listOfProblemSet.isEmpty()) {
			Log.d(Constants.LOG_TAG, "Cannot read from drive. Create sample problems");
			
			listOfProblemSet = GetSampleProblems();
			for(ProblemSet problemSet : listOfProblemSet) {
				try {
					OutputStream stream = this.openFileOutput(System.currentTimeMillis() + ".xml", 0);
					problemSet.dumpToXml(stream);
					stream.close();
				} catch(IOException e) {
					Log.e(Constants.LOG_TAG, e.getLocalizedMessage());
				}
			}
		}
		
		LinearLayout metadataList = (LinearLayout) findViewById(R.id.metadata_list);
		metadataList.removeAllViews();
		for(ProblemSet problemSet : listOfProblemSet) {
			ProblemMetadataView view = new ProblemMetadataView(problemSet, this, null);
			metadataList.addView(view);
		}
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
			startActivityForResult(new Intent(this, PreferenceActivity.class), 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
