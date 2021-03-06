package info.silphire.petitflashmemory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	private String answer;
	
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
		this.choice = new ArrayList<String>();
		
		for(Node child = problemNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if("statement".equals(child.getLocalName())) {
				this.statement = child.getTextContent();
			} else if("choice".equals(child.getLocalName())) {
				Node answerAttr = child.getAttributes().getNamedItem("answer");
				if(answerAttr != null) {
					answer = answerAttr.getTextContent();
				}
				
				choice.add(child.getTextContent());
			} else {
				// 知らない要素は無視
			}
		}
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
	
	public String getAnswer() {
		return this.answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
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
			if(choice == answer) {	// answerはList<String> choiceの中の一要素を表しているはずなのでこれでよい
				writer.write("  <choice answer=\"true\">" + choice + "</choice>\n");
			} else {
				writer.write("  <choice>" + choice + "</choice>\n");
			}
		}
		
		writer.write(prefix);
		writer.write("</problem>\n");
		
		writer.write("\n");
		writer.flush();
	}
}
