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
 * 問題を扱うクラスです。
 * 
 * 問題のファイルフォーマットは以下に示す通りです。
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
	 * 問題をReaderから解析します。
	 * うまく解析できなかった場合はIllegalFormatExceptionを投げます。
	 * 
	 * @param node <problem>の要素を指しているノードです。
	 * @throws IllegalFormatException Readerからの解析に失敗した時に投げられます。
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
