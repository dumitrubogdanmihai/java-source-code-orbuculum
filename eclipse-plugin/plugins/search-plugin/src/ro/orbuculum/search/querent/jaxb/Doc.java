package ro.orbuculum.search.querent.jaxb;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;

@XmlRootElement
public class Doc {
  private String project;
  private String path;
  private String className;
  private String method;
  private Integer lineStart;
  private Integer lineEnd;

	@XmlPath("arr[@name=\"offset-start\"]/long/text()")
  public Integer getLineStart() {
    return lineStart;
  }

  public void setLineStart(Integer lineStart) {
    this.lineStart = lineStart;
  }

  @XmlPath("arr[@name=\"offset-end\"]/long/text()")
  public Integer getLineEnd() {
    return lineEnd;
  }

  public void setLineEnd(Integer lineEnd) {
    this.lineEnd = lineEnd;
  }


  @XmlPath("str[@name=\"project\"]/text()")
  public String getProject() {
    return project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  @XmlPath("str[@name=\"path\"]/text()")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }


  @XmlPath("str[@name=\"class\"]/text()")
  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  @XmlPath("str[@name=\"method\"]/text()")
  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }
}
