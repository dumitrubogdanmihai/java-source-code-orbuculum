package ro.orbuculum.search.querent.api;

public enum Fields {
	PROJECT("project", true, false),
	PATH("path", true, false),
	CLASS("class", true, false),
	METHOD("method", true, false),
	METHOD_Q_NAME("method-q-name", true, false),
	METHOD_RAW("method-raw", true, false),
	PARAMETER("parameter", true, true),
	OFFSET_START("offset-start", true, false),
	OFFSET_END("offset-end", true, false),
	ID("id", true, false);
	
	private String name;
	private boolean multivalue;
	private boolean stored;
	
	@Override
	public String toString() {
		return this.getName();
	}
	
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
