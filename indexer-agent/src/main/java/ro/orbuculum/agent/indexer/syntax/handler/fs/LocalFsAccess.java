package ro.orbuculum.agent.indexer.syntax.handler.fs;

import java.io.File;

public class LocalFsAccess implements FsAccess {
  @Override
  public boolean exist(String filePath) {
    return new File(filePath).exists();
  }
}
