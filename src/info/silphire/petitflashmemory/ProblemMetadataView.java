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
 * ���ꗗ��\������ۂɁA�P��̖��̃��^�f�[�^�����X�g�����Ĉꗗ����ׂ̃J�X�^���r���[�ł��B
 * 
 * �\�����镨�́A
 * �E��薼
 * �E�쐬��
 * �E���̍쐬����
 * �E��萔
 * �E���̏ڍ�
 * �ł��B
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
	 * �w�肳�ꂽproblemSet��\������悤�ɂ��܂�
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

		// �E�񂹂ɂ��Ă������Ȃ̂ɂȂ��Ă��Ȃ��̂ŁA�ǂ��ɂ��������B
		TextView numProblems = (TextView) layout.findViewById(R.id.numProblems);
		numProblems.setText(String.valueOf(problemSet.getProblemList().size()));
		
		// GestureDetector�̃Z�b�g�A�b�v
		gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO: �^�b�`���ꂽ��o�������BQuestionActivity�ɔ�ԁB
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
				// TODO: ���������ꂽ�ꍇ�̓��^�f�[�^�̏ڍׂ�\������B
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
