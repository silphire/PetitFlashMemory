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
 * 問題の選択画面です。
 * 
 * @author Yuki Mitsui <silphire@gmail.com>
 *
 */
public class MainActivity extends ActionBarActivity {
	/**
	 * 設定されているディレクトリから問題の一覧を取得し、返します。
	 * 
	 * @return　読み込んだ問題の一覧
	 */
	public List<ProblemSet> GetProblems() {
		List<ProblemSet> listProblemSet = new ArrayList<ProblemSet>();
		
		// 問題が存在するディレクトリを取得する
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String problemPath = prefs.getString("problemPath", null);
		Log.d(Constants.LOG_TAG, "problemPath: " + problemPath);
		
		if(problemPath == null) {
			// Preferenceから規定のディレクトリの設定が読み込めない場合
			for(String file : this.fileList()) {
				ProblemSet problemSet = readOneProblemSet(new File(file));
				if(problemSet != null) {
					listProblemSet.add(problemSet);
				}
			}
		} else {
			// 規定のディレクトリの場所が分かった場合
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
	 * ファイルから問題を読み込んで返します
	 * 
	 * @param file
	 * @return
	 */
	protected ProblemSet readOneProblemSet(File file) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
		} catch(FileNotFoundException e) {
			// ファイルが無い場合は単に無視して次を処理する
			Log.d(Constants.LOG_TAG, e.getLocalizedMessage());
			return null;
		}
		
		ProblemSet problemSet = new ProblemSet();
		try {
			problemSet.parseMetadata(stream);
		} catch (IllegalFormatException | SAXException | IOException
				| ParserConfigurationException | ParseException e) {
			// parse出来ないのならば、無視して次に行くだけ。
			Log.d(Constants.LOG_TAG, e.getLocalizedMessage());
		}
		
		try {
			stream.close();
			return problemSet;
		} catch (IOException e) {
			// close出来ないだけならとりあえず無視
			return null;
		}
	}
	
	/**
	 * 架空の問題を表示します (テスト用)
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
		problemSet.setTitle("テスト");
		problemSet.setCreator("名無しの権兵衛");
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
