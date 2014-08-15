package info.silphire.petitflashmemory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

/**
 * 
 * 問題を扱うクラスです。
 * 
 * 問題のファイルフォーマットは以下に示す通りです。
 * 
 * @author Yuki Mitsui <silphire@gmail.com>
 * 
 */
public class ProblemSet implements Serializable {
	private static final long serialVersionUID = 8661999289018304965L;
	
	private String title;
	private String creator;
	private Date createdDate;
	private List<Problem> problemList;
	
	private final NamespaceContext nsContext = new NamespaceContext() {
		private final Map<String, String> nsMap = new HashMap<String, String>() {
			private static final long serialVersionUID = 3673785369003896310L;
			
			{
				put("dc", "http://purl.org/dc/elements/1.1/"); 
			}
		};
		
		@Override
		public String getPrefix(String namespaceURI) {
			return null;
		}

		@Override
		public Iterator<String> getPrefixes(String namespaceURI) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getNamespaceURI(String nsURI) {
			// TODO Auto-generated method stub
			return nsMap.get(nsURI);
		}
	};
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getCreator() {
		return this.creator;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Date getCreatedDate() {
		return this.createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public List<Problem> getProblemList() {
		return this.problemList;
	}
	
	public void setProblemList(List<Problem> problemList) {
		this.problemList = problemList;
	}
	
	public ProblemSet() {
		//
	}
	
	/**
	 * 問題をInputStreamから解析します。
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParseException 
	 * @throws IllegalFormatException 
	 * @throws XPathExpressionException 
	 */
	public void parse(InputStream stream) throws SAXException, IOException, ParserConfigurationException, IllegalFormatException, ParseException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		
		// メタデータを取得しておく
		this.parseMetadata(document);
		
		this.problemList = new ArrayList<Problem>();
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(nsContext);
		
		XPathExpression xpExpression = null;
		try {
			xpExpression = xpath.compile("//problem/problemset/problem");
		} catch (XPathExpressionException e) {
			// 問題は1つも取得できなかったという事にする
			Log.d(Constants.LOG_TAG, "Caught XPathExpressionException, continues");
			return;
		}
		
		NodeList nodeList = null;
		try {
			nodeList = (NodeList)xpExpression.evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// 問題は1つも取得できなかった
			Log.d(Constants.LOG_TAG, "Not found any problem element");
			return;
		}
		
		for(int i = 0; i < nodeList.getLength(); ++i) {
			Node nodeProblem = nodeList.item(i);
			Problem problem = new Problem();
		}
	}
	
	/**
	 * 
	 * 問題をInputStreamから解析します。ただし、問題本文は読み込まず、メタデータのみ取得します。
	 * うまく解析できなかった場合はIllegalFormatExceptionを投げます。
	 * 
	 * @param input 読み込み元のReaderです
	 * @throws IllegalFormatException Readerからの解析に失敗した時に投げられます。
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 */
	public void parseMetadata(InputStream stream) throws IllegalFormatException, SAXException, IOException, ParserConfigurationException, ParseException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		this.parseMetadata(document);
	}
	
	/**
	 * 
	 * parseMetadata(InputStream)とparse(InputStream)の双方から共通して呼び出され、メタデータの読み込みに利用されるメソッド。
	 * 
	 * @param document 解析対象のXMLドキュメント
	 * @throws IllegalFormatException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 */
	protected void parseMetadata(Document document) throws IllegalFormatException, SAXException, IOException, ParserConfigurationException, ParseException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(nsContext);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:SSX");
		
		this.creator = parseMetadataElement("//problem/metadata/dc:creator", document, xpath);
		this.title = parseMetadataElement("//problem/metadata/dc:title", document, xpath);
		this.createdDate = sdf.parse(parseMetadataElement("//problem/metadata/dc:create", document, xpath));
	}
	
	/**
	 * 
	 * XPathで指定されたメタデータを取得して返します。
	 * 
	 * @param xpathString　取得したいメタデータの要素を指すXPath
	 * @param document
	 * @param xpath
	 * @return xpathStringが表すXPathで取得できる要素に含まれているメタデータの値
	 */
	private String parseMetadataElement(String xpathString, Document document, XPath xpath) {
		XPathExpression xpExpression = null;
		try {
			xpExpression = xpath.compile(xpathString);
		} catch (XPathExpressionException e) {
			return null;
		}
		
		Node node = null;
		try {
			node = (Node)xpExpression.evaluate(document, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			return null;
		}
		
		return node.getTextContent();
	}
	
	public void dumpToXml(OutputStream stream) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
		
		writer.write("<?xml version=\"1.0\" encoding=\"utf-8\">\n");
		writer.write("<flashmemory xml:lang=\"ja\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n");
		writer.write("  <metadata>\n");
		if(this.title != null && !this.title.isEmpty()) {
			writer.write("    <dc:title>" + this.title + "</dc:title>\n");
		}
		if(this.creator != null && !this.creator.isEmpty()) {
			writer.write("    <dc:creator>" + this.creator + "</dc:creator>\n");
		}
		if(this.createdDate != null) {
			writer.write("    <dc:createdDate>" + this.createdDate.toString() + "</dc:createdDate>\n");
		}
		writer.write("  </metadata>\n");
		
		if(this.problemList != null && !this.problemList.isEmpty()) {
			writer.write("  <problemset>\n");
			
			writer.flush();
			for(Problem problem : this.problemList) {
				problem.dumpToXml(stream, 4);
			}
			writer.flush();
			
			writer.write("  </problemset>\n");
		}
		writer.write("</flashmemory>\n");
		writer.flush();
	}
}
