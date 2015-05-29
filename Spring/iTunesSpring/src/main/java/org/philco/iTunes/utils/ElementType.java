package org.philco.iTunes.utils;

import com.google.common.collect.Maps;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
public enum ElementType {
	  // XCCDF
	  Benchmark("Benchmark"),
	  TestResult("TestResult"),
	  Tailoring("Tailoring"),
	  Group("Group"),
	  Rule("Rule"),
	  Value("Value"),
	  Profile("Profile"),
	  value("value"),
	  ComplexValue("complex-value"),
	  ComplexCheck("complex-check"),
	  Check("check"),
	  CheckExport("check-export"),
	  Select("select"),
	  SetValue("set-value"),
	  SetComplexValue("set-complex-value"),
	  RefineValue("refine-value"),
	  RefineRule("refine-rule"),
	  RuleResult("rule-result"),

	  // OVAL
	  definition("definition"),
	  OvalDefinitions("oval_definitions"),
	  OvalSystemCharacteristics("oval_system_characteristics"),
	  OvalResults("oval_results"),
	  OvalVariables("oval_variables"),
	  ObjectComponent("object_component"),
	  LiteralComponent("literal_component"),
	  VariableComponent("variable_component"),

	  // OVAL functions for variable evaluation
	  Arithmetic("arithmetic"),
	  Begin("begin"),
	  Concat("concat"),
	  End("end"),
	  EscapeRegex("escape_regex"),
	  Split("split"),
	  Substring("substring"),
	  TimeDifference("time_difference"),
	  RegexCapture("regex_capture"),
	  Unique("unique"),
	  Count("count"),

	  // CPE
	  CpePlatform("platform"),
	  CpeList("cpe-list"),

	  // SCAP
	  DataStreamCollection("data-stream-collection"),
	  DataStream("data-stream"),
	  Dictionaries("dictionaries"),
	  CheckLists("checklists"),
	  Checks("checks"),
	  Component("component"),
	  ComponentRef("component-ref"),

	  // ARF
	  AssetReportCollection("asset-report-collection"),

	  // OCIL
	  Ocil("ocil"),

	  Unexpected("Unexpected");

	  private static Logger logger = LoggerFactory.getLogger(ElementType.class);

	  private final String elementName;

	  private ElementType(String elementName) {
	    this.elementName = elementName;
	  }

	  public String getElementName() {
	   return elementName;
	  }

	  public static ElementType fromElement(Element element) {
	    String elementName = element.getName();
	    ElementType type = MapHolder.INSTANCE.namesToType.get(elementName);
	    if (type == null) {
	      logger.debug("Unable to map element " + element.getName() + " to an ElementType");
	      return ElementType.Unexpected;
	    }
	    return type;
	  }

	  private enum MapHolder {

	    INSTANCE;

	    private final Map<String, ElementType> namesToType = Maps.newHashMap();

	    MapHolder() {
	      for (ElementType type : ElementType.values()) {
	        namesToType.put(type.elementName, type);
	      }
	    }
	  }

}
