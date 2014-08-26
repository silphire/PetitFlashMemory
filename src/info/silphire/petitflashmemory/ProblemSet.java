package info.silphire.petitflashmemory;

import java.io.BufferedWriter;
import java.io.FileInputStream;
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
import java.util.Locale;
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
 * ���������N���X�ł��B
 * 
 * ���̃t�@�C���t�H�[�}�b�g�͈ȉ��Ɏ����ʂ�ł��B
 * 
 * @author Yuki Mitsui <silphire@gmail.com>
 * 
 */
public class ProblemSet implements Serializable {
	private static final long serialVersionUID = 8661999289018304965L;
	
	private String title;
	private String creator;
	private Date createdDate;
	private ArrayList<Problem> problemList;
	private String path;
	
	private static final NamespaceContext nsContext = new NamespaceContext() {
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
	
	public ArrayList<Problem> getProblemList() {
		return this.problemList;
	}
	
	public void setProblemList(ArrayList<Problem> problemList) {
		this.problemList = problemList;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public ProblemSet() {
		//
	}
	
	/**
	 * ����InputStream�����͂��܂��B
	 * 
	 * <problem>
	 *   <statement></statement>
	 *   <choice></choice>
	 * </statement>
	 * 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParseException 
	 * @throws IllegalFormatException 
	 * @throws XPathExpressionException 
	 */
	public void parse(InputStream stream) throws SAXException, IOException, ParserConfigurationException, IllegalFormatException, ParseException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		
		this.parseMetadata(document);
		this.parseProblem(document);
	}
	
	/**
	 * 
	 * ����InputStream�����͂��܂��B�������A���{���͓ǂݍ��܂��A���^�f�[�^�̂ݎ擾���܂��B
	 * ���܂���͂ł��Ȃ������ꍇ��IllegalFormatException�𓊂��܂��B
	 * 
	 * @param input �ǂݍ��݌���Reader�ł�
	 * @throws IllegalFormatException Reader����̉�͂Ɏ��s�������ɓ������܂��B
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
	 * parseMetadata(InputStream)��parse(InputStream)�̑o�����狤�ʂ��ČĂяo����A���^�f�[�^�̓ǂݍ��݂ɗ��p����郁�\�b�h�B
	 * 
	 * @param document ��͑Ώۂ�XML�h�L�������g
	 * @throws IllegalFormatException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 */
	protected void parseMetadata(Document document) throws IllegalFormatException, SAXException, IOException, ParserConfigurationException, ParseException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(nsContext);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:SSX", Locale.getDefault());
		
		this.creator = parseMetadataElement("//problem/metadata/dc:creator", document, xpath);
		this.title = parseMetadataElement("//problem/metadata/dc:title", document, xpath);
		this.createdDate = sdf.parse(parseMetadataElement("//problem/metadata/dc:create", document, xpath));
	}
	
	/**
	 * 
	 * XPath�Ŏw�肳�ꂽ���^�f�[�^���擾���ĕԂ��܂��B
	 * 
	 * @param xpathString�@�擾���������^�f�[�^�̗v�f���w��XPath
	 * @param document
	 * @param xpath
	 * @return xpathString���\��XPath�Ŏ擾�ł���v�f�Ɋ܂܂�Ă��郁�^�f�[�^�̒l
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
	
	/**
	 * �������g��XML�ɏ����o���܂�
	 * 
	 * @param stream �o�͐�̃X�g���[��
	 * @throws IOException
	 */
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
	
	public void parseProblem() throws SAXException, IOException, ParserConfigurationException {
		FileInputStream stream = new FileInputStream(this.path);
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		this.parseProblem(document);
	}
	
	/**
	 * ���^�f�[�^��ǂݍ��ݍς݂̖��ɂ��āA���{�̂�ǂݍ���
	 * 
	 */
	public void parseProblem(Document document) {
		
		this.problemList = new ArrayList<Problem>();
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(nsContext);
		
		XPathExpression xpExpression = null;
		try {
			xpExpression = xpath.compile("//problem/problemset/problem");
		} catch (XPathExpressionException e) {
			// ����1���擾�ł��Ȃ������Ƃ������ɂ���
			Log.d(Constants.LOG_TAG, "Caught XPathExpressionException, continues");
			return;
		}
		
		NodeList nodeList = null;
		try {
			nodeList = (NodeList)xpExpression.evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// ����1���擾�ł��Ȃ�����
			Log.d(Constants.LOG_TAG, "Not found any problem element");
			return;
		}
		
		// �����擾�ł���̂ŁA1�₸��parse���ĕۑ��B
		for(int i = 0; i < nodeList.getLength(); ++i) {
			Node nodeProblem = nodeList.item(i);
			Problem problem = new Problem();

			for(int j = 0; j < nodeProblem.getChildNodes().getLength(); ++j) {
				Node nodeText = nodeProblem.getChildNodes().item(j);
				switch(nodeText.getNodeName()) {
				case "statement":
					if(problem.getStatement() == null) {
						problem.setStatement(nodeText.getTextContent());
					} else {
						// ����<statement>�͉ߋ��ɑ��݂��Ă���
						// �G���[���o�����Ƃ��l�������ǁA�P���ɖ�������悤�ɂ���B�ŏ��Ɍ��ꂽ<statement>�݂̂��L���B
					}
					break;
				case "choice":
					List<String> choice = problem.getChoice();
					if(choice.isEmpty()) {
						choice = new ArrayList<String>();
					}
					choice.add(nodeText.getTextContent());
					problem.setChoice(choice);
				default:
					// �m��Ȃ��m�[�h�͒P���ɖ�������
					break;
				}
			}
		}
	}
}
