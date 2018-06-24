package ro.orbuculum.agent.indexer.syntax.handler.fs;

import org.kohsuke.github.GHRepository;

public class GithubFsAccess implements FsAccess {

  private GHRepository repo;

  public GithubFsAccess(GHRepository repo) {
    this.repo = repo;
  }
  
  @Override
  public boolean exist(String filePath) {
    try {
      repo.getFileContent(filePath);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
