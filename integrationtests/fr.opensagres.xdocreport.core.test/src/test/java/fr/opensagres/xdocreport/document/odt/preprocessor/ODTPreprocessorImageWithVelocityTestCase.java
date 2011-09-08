/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.document.odt.preprocessor;

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityDocumentFormatter;

public class ODTPreprocessorImageWithVelocityTestCase extends TestCase {

	private static final String LOGO_IMAGE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+ "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
			+ "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
			+ "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
			+ "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" "
			+ "xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
			+ "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\">"
			+ "<text:p text:style-name=\"Standard\">"
			+ "<draw:frame draw:style-name=\"fr1\" draw:name=\"logo\" text:anchor-type=\"paragraph\" svg:x=\"69.96pt\" svg:y=\"18.31pt\" svg:width=\"21pt\" svg:height=\"22.51pt\" draw:z-index=\"0\">"
			+ "<draw:image xlink:href=\"Pictures/100000000000001C0000001EE8812A78.png\" xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>"
			+ "</draw:frame>Project logo :</text:p>"
			+ "</office:document-content>";

	public void testImageWithNullFieldsMetadata() throws Exception {
		ODTPreprocessor preprocessor = new ODTPreprocessor();
		StringReader reader = new StringReader(LOGO_IMAGE_XML);
		StringWriter writer = new StringWriter();

		FieldsMetadata metadata = null;
		IDocumentFormatter formatter = new VelocityDocumentFormatter();

		preprocessor.preprocess("test", reader, writer, null, metadata,
				formatter, null);

		assertEquals(LOGO_IMAGE_XML, writer.toString());
	}

	public void testImageWithBadFieldsMetadata() throws Exception {
		ODTPreprocessor preprocessor = new ODTPreprocessor();
		StringReader reader = new StringReader(LOGO_IMAGE_XML);
		StringWriter writer = new StringWriter();

		FieldsMetadata metadata = new FieldsMetadata();
		metadata.addFieldAsImage("XXX");
		IDocumentFormatter formatter = new VelocityDocumentFormatter();

		preprocessor.preprocess("test", reader, writer, null, metadata,
				formatter, null);

		assertEquals(LOGO_IMAGE_XML, writer.toString());
	}

	public void testImageWithSimpleField() throws Exception {
		ODTPreprocessor preprocessor = new ODTPreprocessor();
		StringReader reader = new StringReader(LOGO_IMAGE_XML);
		StringWriter writer = new StringWriter();

		FieldsMetadata metadata = new FieldsMetadata();
		metadata.addFieldAsImage("logo");
		IDocumentFormatter formatter = new VelocityDocumentFormatter();

		preprocessor.preprocess("test", reader, writer, null, metadata,
				formatter, null);

		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
						+ "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
						+ "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
						+ "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" "
						+ "xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
						+ "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\">"
						+ "<text:p text:style-name=\"Standard\">"
						+ "<draw:frame draw:style-name=\"fr1\" draw:name=\"logo\" text:anchor-type=\"paragraph\" svg:x=\"69.96pt\" svg:y=\"18.31pt\" svg:width=\"21pt\" svg:height=\"22.51pt\" draw:z-index=\"0\">"
						+ "<draw:image "

						+ "xlink:href=\"${"
						+ IDocumentFormatter.IMAGE_REGISTRY_KEY
						+ ".registerImage($logo)}\" "

						+ "xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>"
						+ "</draw:frame>Project logo :</text:p>"
						+ "</office:document-content>", writer.toString());
	}

	public void testImageWithListFieldInTable() throws Exception {
		ODTPreprocessor preprocessor = new ODTPreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
						+ "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
						+ "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
						+ "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" "
						+ "xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
						+ "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\">"
						+ "<table:table table:name=\"Tableau1\" table:style-name=\"Tableau1\">"
						+ "<table:table-column table:style-name=\"Tableau1.A\"/>"
						+ "<table:table-column table:style-name=\"Tableau1.B\"/>"
						+ "<table:table-column table:style-name=\"Tableau1.C\"/>"
						+ "<table:table-column table:style-name=\"Tableau1.D\"/>"
						+ "<table:table-row table:style-name=\"Tableau1.1\">"
						+ "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"P1\">Name</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"P2\">Last name</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"P1\">Mail</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"P1\">Photo</text:p>"
						+ "</table:table-cell>"
						+ "</table:table-row>"
						+ "<table:table-row table:style-name=\"Tableau1.1\">"
						+ "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"Standard\">"
						+ "<text:text-input text:description=\"\">$developers.Name</text:text-input>"
						+ "</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"Standard\">"
						+ "<text:text-input text:description=\"\">$developers.LastName</text:text-input>"
						+ "</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"Standard\">"
						+ "<text:text-input text:description=\"\">$developers.Mail</text:text-input>"
						+ "</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"Standard\">"
						+ "<draw:frame draw:style-name=\"fr1\" draw:name=\"developers.Photo\" text:anchor-type=\"paragraph\" svg:width=\"21pt\" svg:height=\"22.51pt\" draw:z-index=\"0\">"
						+ "<draw:image xlink:href=\"Pictures/100000000000001C0000001EE8812A78.png\" xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>"
						+ "</draw:frame>" + "</text:p>" + "</table:table-cell>"
						+ "</table:table-row>" 						
						+ "</table:table>"
						+ "</office:document-content>");
		StringWriter writer = new StringWriter();

		FieldsMetadata metadata = new FieldsMetadata();
		metadata.addFieldAsList("developers.Photo");
		metadata.addFieldAsImage("developers.Photo");
		IDocumentFormatter formatter = new VelocityDocumentFormatter();

		preprocessor.preprocess("test", reader, writer, null, metadata,
				formatter, null);

		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
						+ "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
						+ "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
						+ "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" "
						+ "xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
						+ "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\">"
						+ "<table:table table:name=\"Tableau1\" table:style-name=\"Tableau1\">"
						+ "<table:table-column table:style-name=\"Tableau1.A\"/>"
						+ "<table:table-column table:style-name=\"Tableau1.B\"/>"
						+ "<table:table-column table:style-name=\"Tableau1.C\"/>"
						+ "<table:table-column table:style-name=\"Tableau1.D\"/>"
						+ "<table:table-row table:style-name=\"Tableau1.1\">"
						+ "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"P1\">Name</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"P2\">Last name</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"P1\">Mail</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"P1\">Photo</text:p>"
						+ "</table:table-cell>"
						+ "</table:table-row>"

						+ "#foreach( $item_developers in $developers)"
												
						+ "<table:table-row table:style-name=\"Tableau1.1\">"
						+ "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"Standard\">"
						// +
						// "<text:text-input text:description=\"\">$developers.Name</text:text-input>"
						+ "$developers.Name"
						+ "</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"Standard\">"
						// +
						// "<text:text-input text:description=\"\">$developers.LastName</text:text-input>"
						+ "$developers.LastName"
						+ "</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"Standard\">"
						// +
						// "<text:text-input text:description=\"\">$developers.Mail</text:text-input>"
						+ "$developers.Mail"
						+ "</text:p>"
						+ "</table:table-cell>"
						+ "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
						+ "<text:p text:style-name=\"Standard\">"
						+ "<draw:frame draw:style-name=\"fr1\" draw:name=\"developers.Photo\" text:anchor-type=\"paragraph\" svg:width=\"21pt\" svg:height=\"22.51pt\" draw:z-index=\"0\">"
						+ "<draw:image "

						+ "xlink:href=\"${"
						+ IDocumentFormatter.IMAGE_REGISTRY_KEY
						+ ".registerImage($item_developers.Photo)}\" "

						+ "xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>"
						+ "</draw:frame>" + "</text:p>" + "</table:table-cell>"
						+ "</table:table-row>" 
						
						+ "#end"
						
						+ "</table:table>"
						+ "</office:document-content>", writer.toString());

	}
}