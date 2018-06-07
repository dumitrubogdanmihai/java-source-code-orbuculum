package ro.orbuculum.search;

public class SearchResultEntity {
	
	private String clazz;
	private String method;
	private String path;
	private int from;
	private int to;
	
	public SearchResultEntity(String clazz, String method, String path, int from, int to) {
		this.clazz = clazz;
		this.method = method;
		this.path = path;
		this.from = from;
		this.to = to;
	
	}
	public String getClazz() {
		return clazz;
	}
	public String getMethod() {
		return method;
	}
	public String getPath() {
		return path;
	}
	public int getFrom() {
		return from;
	}
	public int getTo() {
		return to;
	}
}
