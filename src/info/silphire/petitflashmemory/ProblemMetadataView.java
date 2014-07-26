package info.silphire.petitflashmemory;

import java.text.DateFormat;
import java.util.Locale;

import android.content.Context;
import android.text.method.DateTimeKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
		
		TextView numProblems = (TextView) layout.findViewById(R.id.numProblems);
		numProblems.setText(String.valueOf(problemSet.getProblemList().size()));
	}
	
}
