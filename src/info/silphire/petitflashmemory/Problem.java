package info.silphire.petitflashmemory;

import java.util.IllegalFormatException;
import java.util.List;

import org.w3c.dom.Node;

/**
 * 
 * ���������N���X�ł��B
 * 
 * ���̃t�@�C���t�H�[�}�b�g�͈ȉ��Ɏ����ʂ�ł��B
 * 
 * <problem>
 *   <statement></statement>
 *   <choice></choice>
 *   <choice></choice>
 *   <choice></choice>
 *   <choice></choice>
 * </problem>
 * 
 * @author Yuki Mitsui <silphire@gmail.com>
 * 
 */
public class Problem {
	private String statement;
	private List<String> choice;
	
	public Problem() {
		//
	}
	
	/**
	 * 
	 * ����Reader�����͂��܂��B
	 * ���܂���͂ł��Ȃ������ꍇ��IllegalFormatException�𓊂��܂��B
	 * 
	 * @param node <problem>�̗v�f���w���Ă���m�[�h�ł��B
	 * @throws IllegalFormatException Reader����̉�͂Ɏ��s�������ɓ������܂��B
	 */
	public void parse(Node problemNode) throws IllegalFormatException {
		;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public List<String> getChoice() {
		return choice;
	}

	public void setChoice(List<String> choice) {
		this.choice = choice;
	}
}