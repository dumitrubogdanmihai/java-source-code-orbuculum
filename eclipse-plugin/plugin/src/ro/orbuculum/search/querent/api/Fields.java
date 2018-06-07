package ro.orbuculum.search.querent.api;

public enum Fields {
	METHOD("method", false, true),
	METHOD_Q_NAME("method-q-name", false, true),
	METHOD_RAW ("method-raw", false, false),
	parameter("parameter", true, true),
	JAVADOC("javadoc", false, false),
	OFFSET("offset", false, true),
	JAVA_KEYWORD("java-keyword", true, false),
	COMMENT("comment", true, false),
	OBJECT("object", true, false),
	METHOD_CALL("method-call", true, false);
	
	private String name;
	private boolean multivalue;
	private boolean stored;
	Fields(String name, boolean multivalue, boolean stored) {
		this.setName(name);
		this.setMultivalue(multivalue);
		this.setStored(stored);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMultivalue() {
		return multivalue;
	}
	public void setMultivalue(boolean multivalue) {
		this.multivalue = multivalue;
	}
	public boolean isStored() {
		return stored;
	}
	public void setStored(boolean stored) {
		this.stored = stored;
	}
}
