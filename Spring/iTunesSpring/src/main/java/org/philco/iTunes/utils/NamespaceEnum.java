package org.philco.iTunes.utils;

	import com.google.common.collect.Maps;
	import org.jdom2.Namespace;

	import java.util.Collection;
	import java.util.Collections;
	import java.util.Map;

	public enum NamespaceEnum {

	  XMLSIG("ds", "http://www.w3.org/2000/09/xmldsig#"),
	  XSI("xsi","http://www.w3.org/2001/XMLSchema-instance"),
	  XLINK("xlink","http://www.w3.org/1999/xlink"),
	  CATALOG("cat","urn:oasis:names:tc:entity:xmlns:xml:catalog"),
	  OVAL("oval5","http://oval.mitre.org/XMLSchema/oval-common-5"),
	  OVAL_DEFS("oval5-defs","http://oval.mitre.org/XMLSchema/oval-definitions-5"),
	  OVAL_AIX_DEFS("oval5-defs-aix","http://oval.mitre.org/XMLSchema/oval-definitions-5#aix"),
	  OVAL_ESX_DEFS("oval5-defs-esx","http://oval.mitre.org/XMLSchema/oval-definitions-5#esx"),
	  OVAL_HPUX_DEFS("oval5-defs-hpux","http://oval.mitre.org/XMLSchema/oval-definitions-5#hpux"),
	  OVAL_IND_DEFS("oval5-defs-independent","http://oval.mitre.org/XMLSchema/oval-definitions-5#independent"),
	  OVAL_LINUX_DEFS("oval5-defs-linux","http://oval.mitre.org/XMLSchema/oval-definitions-5#linux"),
	  OVAL_MACOS_DEFS("oval5-defs-macos","http://oval.mitre.org/XMLSchema/oval-definitions-5#macos"),
	  OVAL_SOLARIS_DEFS("oval5-defs-solaris","http://oval.mitre.org/XMLSchema/oval-definitions-5#solaris"),
	  OVAL_UNIX_DEFS("oval5-defs-unix","http://oval.mitre.org/XMLSchema/oval-definitions-5#unix"),
	  OVAL_WINDOWS_DEFS("oval5-defs-windows", "http://oval.mitre.org/XMLSchema/oval-definitions-5#windows"),
	  OVAL_VARIABLES("oval5-variables","http://oval.mitre.org/XMLSchema/oval-variables-5"),
	  OVAL_RESULTS("oval-res", "http://oval.mitre.org/XMLSchema/oval-results-5"),
	  OVAL_SC("oval-sc", "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"),
	  WIN_SC("win-sc", "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"),
	  IND_SC("ind-sc", "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"),
	  SCAP("scap","http://scap.nist.gov/schema/scap/source/1.2"),
	  SCAP_REL("scap_rel","http://scap.nist.gov/vocabulary/scap/relationships/1.0#"),
	  XCCDF_11("xccdf11","http://checklists.nist.gov/xccdf/1.1"),
	  XCCDF_12("xccdf12","http://checklists.nist.gov/xccdf/1.2"),
	  TRIPWIRE("tripwire","http://tripwire.com"),
	  CPE_LANGUAGE("cpe", "http://cpe.mitre.org/language/2.0"),
	  CPE_DICT("cpe2-dict", "http://cpe.mitre.org/dictionary/2.0"),
	  ARF("arf", "http://scap.nist.gov/schema/asset-reporting-format/1.1"),
	  ARF_VOCAB("arfvocab", "http://scap.nist.gov/vocabulary/arf/relationships/1.0#"),
	  REPORTING_CORE("core", "http://scap.nist.gov/schema/reporting-core/1.1"),
	  NIST_AI("ai", "http://scap.nist.gov/schema/asset-identification/1.1"),
	  DUBLIN_CORE("dc", "http://purl.org/dc/elements/1.1/");

	  private final Namespace namespace;

	  private NamespaceEnum(String prefix, String uri) {
	    this.namespace = Namespace.getNamespace(prefix, uri);
	  }

	  public Namespace getNamespace() {
	    return namespace;
	  }

	  public String getUri() {
	    return namespace.getURI();
	  }

	  public String getPrefix() {
	    return namespace.getPrefix();
	  }

	  /**
	   * When building a single OVAL document from multiple OVAL documents, it is possible
	   * that the source documents may use different namespace prefixes. So we need to map
	   * the Namespaces from the source documents to a single standard Namespace so all
	   * our elements are using the same prefix. This method helps with that.
	   */
	  public static Namespace getNamespace(String uri) {
	    return NamespaceMap.INSTANCE.namespacesByUri.get(uri);
	  }

	  public static Collection<Namespace> getNamespaces() {
	    return Collections.unmodifiableCollection(NamespaceMap.INSTANCE.namespacesByUri.values());
	  }

	  private static enum NamespaceMap {

	    INSTANCE;

	    private Map<String, Namespace> namespacesByUri;

	    private NamespaceMap() {
	      namespacesByUri = Maps.newHashMap();
	      for (NamespaceEnum namespaceEnum : NamespaceEnum.values()) {
	        Namespace namespace = namespaceEnum.getNamespace();
	        namespacesByUri.put(namespace.getURI(), namespace);
	      }
	    }
	  }

}
