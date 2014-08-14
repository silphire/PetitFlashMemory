package info.silphire.petitflashmemory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
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
public class Problem implements Serializable {
	private static final long serialVersionUID = -5505052151717124082L;
	
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
	
	public void dumpToXml(OutputStream stream, int nIndent) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
		
		StringBuilder prefixBuilder = new StringBuilder();
		for(int i = 0; i < nIndent; ++i) {
			prefixBuilder.append(' ');
		}
		String prefix = prefixBuilder.toString();
		
		writer.write(prefix);
		writer.write("<problem>\n");
		
		writer.write(prefix);
		writer.write("  <statement>" + this.getStatement() + "</statement>\n");
		
		for(String choice : this.getChoice()) {
			writer.write(prefix);
			writer.write("  <choice>" + choice + "</choice>\n");
		}
		
		writer.write(prefix);
		writer.write("</problem>\n");
		
		writer.write("\n");
		writer.flush();
	}
}
