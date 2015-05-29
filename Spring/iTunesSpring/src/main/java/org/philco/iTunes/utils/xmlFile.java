package org.philco.iTunes.utils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.io.FileBackedOutputStream;
import com.google.common.io.InputSupplier;
import org.apache.commons.io.IOUtils;
import org.jdom2.*;
import org.jdom2.filter.Filter;
import org.jdom2.output.Format;
import org.jdom2.output.LineSeparator;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.JDOMSource;
import org.jdom2.xpath.XPathBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.*;
import javax.annotation.Nullable;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;

public class xmlFile {
	  public static final Function<Attribute, String> ATTR_TO_STRING_FUNCTION = new Function<Attribute, String>() {
		    @Override
		    public String apply(@Nullable Attribute attr) {
		      return (attr == null) ? null : attr.getValue();
		    }
		  };

		  public static final Function<Element, String> ELEMENT_TO_NAME_FUNCTION = new Function<Element, String>() {
		    @Override
		    public String apply(@Nullable Element element) {
		      return element == null ? null : element.getName();
		    }
		  };

		  public static Function<Element, String> ELEMENT_TO_TEXT_CONTENT = new Function<Element, String>() {
		    @Override
		    public String apply(@Nullable Element element) {
		      return element == null ? null : element.getText();
		    }
		  };

		  /**
		   * Returns the namespace of the specified element, but as one definied in {@link NamespaceEnum}.
		   * This is so that our generated elements could use the same "standardized" prefix.
		   */
		  public static Namespace getStandardizedNamespace(Element element) {
		    return NamespaceEnum.getNamespace(element.getNamespace().getURI());
		  }

		  /**
		   * Returns true if the specified attribute value is "1" or "true". If the
		   * attribute is not set on the element, it returns the defaultValue.
		   */
		  public static boolean getBooleanAttribute(
		      Element element, String attributeName, boolean defaultValue) {
		    final Attribute attr = element.getAttribute(attributeName);
		    if (attr == null) {
		      return defaultValue;
		    }
		    final String value = attr.getValue();
		    return "1".equals(value) || "true".equals(value.toLowerCase());
		  }

		  /**
		   * Sets the attribute from the source element to the destination element. The attribute is not
		   * set if it does not exist/set on the source element.
		   *
		   * @param dest     destination element where to set the attribute.
		   * @param destAttr name of the attribute to be set on the destination element.
		   * @param source   element where the attribute value is defined.
		   * @param srcAttr  name of the attribute on the source element.
		   */
		  public static void setOptionalAttribute(Element dest, String destAttr, Element source, String srcAttr) {
		    String value = source.getAttributeValue(srcAttr);
		    if (value != null) {
		      dest.setAttribute(destAttr, value);
		    }
		  }

		  // NOTE: XPathExpression is not thread-safe
		  private static ThreadLocal<XPathExpression<Element>> elementByIdXpath = new ThreadLocal<XPathExpression<Element>>() {
		    @Override
		    public XPathExpression<Element> get() {
		      XPathBuilder<Element> builder =
		          new XPathBuilder<Element>(".//*[@id=$elementId]", org.jdom2.filter.Filters.element());
		      builder.setVariable("elementId", null);
		      return builder.compileWith(XPathFactory.instance());
		    }
		  };

		  /**
		   * Returns a descendant of the context element that has the specified @id attribute
		   *
		   * @param context The element whose descendants will be searched
		   * @param id      The value of the @id attribute to search for
		   * @return The descendant element with the matching ID, if found. Null otherwise.
		   * @throws IllegalStateException If more than one descendant has the @id attribute
		   */
		  public static Element getDescendantById(Element context, String id) {
		    XPathExpression<Element> elementById = elementByIdXpath.get();
		    elementById.setVariable("elementId", id);
		    List<Element> elements = elementById.evaluate(context);
		    if (elements == null || elements.isEmpty()) {
		      return null;
		    }
		    if (elements.size() > 1) {
		      throw new IllegalStateException("Multiple elements found with ID " + id);
		    }
		    return elements.get(0);
		  }

		  /**
		   * Gets immediate children with the specified names
		   */
		  public static Iterable<Element> getChildren(final Element parent, final Filter<Element> filter) {
		    List<Element> children = parent.getChildren();
		    return Iterables.filter(children, new Predicate<Element>() {
		      @Override
		      public boolean apply(@Nullable Element input) {
		        return input != null && filter.matches(input);
		      }
		    });
		  }

		  /**
		   * Writes the XML for the specified JDOM element to a String
		   */
		  public static String toString(Element element) {
		    XMLOutputter outputter = getOutputter();
		    return outputter.outputString(element);
		  }

		  /**
		   * Returns an {@link InputSupplier} for the specified XML document. This is equivalent to
		   * <code>toSupplier(document, 1MB)</code>.
		   * @see #toSupplier(Document, int)
		   */
		  public static InputSupplier<InputStream> toSupplier(Document document) throws IOException {
		    return toSupplier(document, 1024 * 1024);
		  }

		  /**
		   * Returns an {@link InputSupplier> for the specified XML document. The provided threshold,
		   * in bytes, specifies the size beyond which the XML will be kept on disk in a temp file
		   * instead of in memory.
		   * <p/>
		   * The bytes stream of the document is "raw" (i.e. unchanged) in UTF-8. If you want to pretty
		   * print a document, use one of the {@link @write} methods instead.
		   * <p/>
		   * @param document whose input stream is to be supplied.
		   * @param threshold number of bytes before the stream should switch from buffering to file.
		   * @return a supplier of stream for the XML document.
		   */
		  public static InputSupplier<InputStream> toSupplier(Document document, int threshold) throws IOException {
		    FileBackedOutputStream out = new FileBackedOutputStream(threshold, true);
		    try {
		      // use a raw outputter so not to change text content of the document
		      XMLOutputter outputter = getRawOutputter();
		      outputter.output(document, out);
		    } finally {
		      IOUtils.closeQuietly(out);
		    }
		    return out.getSupplier();
		  }

		  /**
		   * Writes the XML document to the specified Writer
		   *
		   * @param document The document to write
		   * @param writer   The Writer to write to
		   * @return
		   */
		  public static void write(Document document, Writer writer) throws IOException {
		    XMLOutputter outputter = getOutputter();
		    outputter.output(document, writer);
		  }

		  public static void write(Document document, OutputStream stream) throws IOException {
		    XMLOutputter outputter = getOutputter();
		    outputter.output(document, stream);
		  }

		  public static String toString(Document document) {
		    XMLOutputter outputter = getOutputter();
		    return outputter.outputString(document);
		  }

		  public static XMLOutputter getOutputter() {
		    Format format = Format.getPrettyFormat();
		    format.setLineSeparator(LineSeparator.SYSTEM);
		    format.setEncoding("UTF-8");
		    return new XMLOutputter(format);
		  }

		  /**
		   * Returns an XMLOutputter that uses the raw Format and the default XMLOutputProcessor.
		   */
		  public static XMLOutputter getRawOutputter() {
		    Format format = Format.getRawFormat(); // TextMode.PRESERVE & UTF-8 are defaults
		    return new XMLOutputter(format);
		  }

		  /**
		   * Validates that the specified element is one of the specified types. If not,
		   * a runtime exception is thrown
		   *
		   * @param element The element to check
		   * @param types   List of valid types for the element
		 * @throws UnexpectedElementTypeException 
		   */
		  public static void validateType(Element element, ElementType... types) throws UnexpectedElementTypeException {
		    String name = element.getName();
		    for (ElementType type : types) {
		      if (name.equals(type.getElementName())) {
		        return;
		      }
		    }
		    throw new UnexpectedElementTypeException(element.getQualifiedName(), types);
		  }

		  /**
		   * Compiles the specified xpath expression.
		   *
		   * @param xpath  expression i.e. the query.
		   * @param filter of the result type.
		   */
		  public static <T> XPathExpression<T> compileXpath(String xpath, Filter<T> filter) {
		    XPathFactory factory = XPathFactory.instance();
		    return factory.compile(xpath, filter, null, NamespaceEnum.getNamespaces());
		  }

		  /**
		   * Compiles an xpath expression with variables. Variables in the xpath query are of $varname syntax.
		   * The returned xpath expression doesn't have any value set for the variables; the caller must set
		   * their values using XPathExpression.setVariable().
		   * <p/>
		   * Example:<pre>
		   * XPathExpression<Element> xpathExpression = JdomUtils.compileXpath(
		   *   "scap:component[@id=$componentId]/*[@id=$benchmarkId][xccdf12:version=$version or xccdf11:version=$version]",
		   *   Sets.newHashSet("componentId", "benchmarkId", "version"),
		   *   Filters.element());   
		   * </pre>
		   * @param xpath query expression to compile.
		   * @param varnames variable names used in the query.
		   * @param filter of the result type.
		   */
		  public static <T> XPathExpression<T> compileXpath(String xpath, Set<String> varnames, Filter<T> filter) {
		    XPathBuilder<T> builder = new XPathBuilder<T>(xpath, filter);
		    builder.setNamespaces(NamespaceEnum.getNamespaces());
		    for (String varname: varnames) {
		      builder.setVariable(varname, null);
		    }

		    return builder.compileWith(XPathFactory.instance());
		  }

		  /**
		   * Evaluates the specified xpath expression against the specified context.
		   *
		   * @param context    against which to process the query.
		   * @param expression the compiled xpath.
		   * @see #compileXpath
		   */
		  public static <T> List<T> evaluateXpath(Element context, XPathExpression<T> expression) {
		    return expression.evaluate(context);
		  }

		  /**
		   * Evaluates the specified xpath expression that returns the specified JDOM type. For instance if
		   * the expression returns attributes, the Filter must be Filters.attribute().
		   *
		   * @param context context against which to process the query.
		   * @param xpath   expression i.e. the query.
		   * @param filter  of the result type.
		   */
		  public static <T> List<T> evaluateXpath(Parent context, String xpath, Filter<T> filter) {
		    XPathExpression<T> expression = compileXpath(xpath, filter);
		    return expression.evaluate(context);
		  }

		  /**
		   * Evaluates an xpath expression that returns Elements.
		   */
		  public static List<Element> evaluateXpath(Parent context, String expression) {
		    return evaluateXpath(context, expression, org.jdom2.filter.Filters.element());
		  }

		  public static Element evaluateXpathFirst(Parent context, String expression) {
		    List<Element> elements = evaluateXpath(context, expression);
		    return elements.isEmpty() ? null : elements.get(0);
		  }

		  /**
		   * Evaluates an Element XPath expression for which there is exactly one match.
		   * If the expression does not match any elements, or if it matches more than
		   * one element, an unchecked exception is thrown
		   *
		   * @param context    The element in which to evaluate the expression
		   * @param expression The expression
		   * @return The single Element that matches the expression
		   * @throws NoSuchElementException   If no element matches the expression
		   * @throws IllegalArgumentException If the expression matches more than one element
		   */
		  public static Element evaluateXpathOnly(Parent context, String expression)
		      throws NoSuchElementException, IllegalArgumentException {
		    return Iterables.getOnlyElement(evaluateXpath(context, expression));
		  }

		  public static Document transform(Document source, InputStream stylesheet)
		      throws TransformerException {
		    StreamSource stylesheetSource = new StreamSource(stylesheet);
		    return transform(source, stylesheetSource, Collections.<String, Object>emptyMap());
		  }

		  public static Document transform(Document source, Document stylesheet)
		      throws TransformerException {
		    return transform(source, stylesheet, Collections.<String, Object>emptyMap());
		  }

		  /**
		   * Transform the specified document using the specified stylesheet. If the stylesheet has parameters,
		   * they may be set using a map of parameter name to value. Use an empty map when there's no param.
		   *
		   * @param source     document to be transformed
		   * @param stylesheet for transforming the above document
		   * @param params     parameters for the stylesheet
		   * @return transformed document
		   */
		  public static Document transform(Document source, Document stylesheet, Map<String, Object> params)
		      throws TransformerException {
		    JDOMSource stylesheetSource = new JDOMSource(stylesheet);
		    return transform(source, stylesheetSource, params);
		  }

		  private static Document transform(Document source, Source stylesheetSource, Map<String, Object> params)
		      throws TransformerException {

		    TransformerFactory factory = TransformerFactory.newInstance();
		    Templates templates = factory.newTemplates(stylesheetSource);
		    Transformer transformer = templates.newTransformer();
		    for (Map.Entry<String, Object> entry : params.entrySet()) {
		      transformer.setParameter(entry.getKey(), entry.getValue());
		    }

		    JDOMResult result = new JDOMResult();
		    transformer.transform(new JDOMSource(source), result);
		    return result.getDocument();
		  }

}
