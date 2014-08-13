package info.silphire.petitflashmemory;

import java.text.DateFormat;
import java.util.Locale;

import android.content.Context;
import android.text.method.DateTimeKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * 
 * 問題一覧を表示する際に、単一の問題のメタデータをリスト化して一覧する為のカスタムビューです。
 * 
 * 表示する物は、
 * ・問題名
 * ・作成者
 * ・問題の作成日時
 * ・問題数
 * ・問題の詳細
 * です。
 * 
 * @author Yuki Mitsui <silphire@gmail.com>
 *
 */
public class ProblemMetadataView extends TableLayout {
	private ProblemSet problemSet;
	private GestureDetector gestureDetector;
	
	public void setProblemSet(ProblemSet problemSet) {
		this.problemSet = problemSet;
	}

	public ProblemMetadataView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * 指定されたproblemSetを表示するようにします
	 * 
	 * @param problemSet 
	 * @param context 
	 * @param attrs 
	 * 
	 */
	public ProblemMetadataView(ProblemSet problemSet, Context context, AttributeSet attrs) {
		super(context, attrs);

		View layout = inflate(context, R.layout.problem_metadata, this);
		
		TextView title = (TextView) layout.findViewById(R.id.title);
		title.setText(problemSet.getTitle());
		
		TextView creator = (TextView) layout.findViewById(R.id.creator);
		creator.setText(problemSet.getCreator());
		
		TextView createdDate = (TextView) layout.findViewById(R.id.createdDate);
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault());
		createdDate.setText(dateFormat.format(problemSet.getCreatedDate()));

		// 右寄せにしているつもりなのになっていないので、どうにかしたい。
		TextView numProblems = (TextView) layout.findViewById(R.id.numProblems);
		numProblems.setText(String.valueOf(problemSet.getProblemList().size()));
		
		// GestureDetectorのセットアップ
		gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO: タッチされたら出題をする。QuestionActivityに飛ぶ。
				Log.d(Constants.LOG_TAG, "START LESSON: <" + ProblemMetadataView.this.problemSet.getTitle() + ">");
				return true;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO: 長押しされた場合はメタデータの詳細を表示する。
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return true;
	}
}
