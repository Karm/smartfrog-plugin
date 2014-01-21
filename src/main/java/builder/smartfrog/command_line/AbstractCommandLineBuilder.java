package builder.smartfrog.command_line;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import builder.smartfrog.RewriteRule;
import builder.smartfrog.SmartFrogBuilder;
import builder.smartfrog.SmartFrogHost;
import builder.smartfrog.SmartFrogInstance;
import builder.smartfrog.util.Functions;

/**
 * @author jcechace
 */
public abstract class AbstractCommandLineBuilder implements CommandLineBuilder {

  private static final Logger LOGGER = Logger.getLogger(AbstractCommandLineBuilder.class.getName());
  private static final Pattern malformedPathPattern = Pattern.compile("^[\\\\/]([a-zA-Z]:.*)");
  private SmartFrogBuilder builder = null;
  private SmartFrogInstance sfInstance = null;
  private SmartFrogHost host = null;
  private FilePath workspace = null;

  public AbstractCommandLineBuilder(SmartFrogHost host) {
    this.builder = host.getSfAction().getBuilder();
    this.sfInstance = this.builder.getSfInstance();
    this.host = host;
    // TODO: This is so nasty it leaves me speechless. I must investigate why Windows JDK plays with /C: and stop it.
    // Furthermore, do you know, what happens if slave has Remote FS Root set to h:/something? You get /h:/something here :-(
    // Yet, this: .replace("/C:", "") might not be necessary...
    String rawWorspace = host.getSfAction().getOwnerBuild().getWorkspace().getRemote();
    LOGGER.severe("RAWWORKSPACE:" + rawWorspace);
    Matcher m = malformedPathPattern.matcher(rawWorspace);
    if (m.find() && m.group(1) != null && m.group(1).length() > 0) {
      // Yes, the path is malformed, something like /h:/hudson/workspace or /C:/tmp
      LOGGER.severe("Workspapce path was malformed as [" + rawWorspace + "], now it is: [" + m.group(1) + "]");
      rawWorspace = m.group(1);
    }
    this.workspace = new FilePath(new File(rawWorspace));
    LOGGER.severe("RAWWORKSPACE 2:" + rawWorspace);
  }

  public AbstractCommandLineBuilder() {

  }

  public SmartFrogBuilder getBuilder() {
    return builder;
  }

  public void setBuilder(SmartFrogBuilder builder) {
    this.builder = builder;
  }

  public SmartFrogInstance getSfInstance() {
    return sfInstance;
  }

  public void setSfInstance(SmartFrogInstance sfInstance) {
    this.sfInstance = sfInstance;
  }

  public SmartFrogHost getHost() {
    return host;
  }

  public void setHost(SmartFrogHost host) {
    this.host = host;
  }

  public FilePath getWorkspace() {
    return workspace;
  }

  public void setWorkspace(FilePath workspace) {
    this.workspace = workspace;
  }

  public String getSfOpts() {
    return host.getSfOpts() == null ? getBuilder().getSfOpts() : host.getSfOpts();
  }

  public String getJdk() {
    return host.getJdk() == null ? "" : host.getJdk();
  }

  public String getSfInstancePath() {
    return getSfInstance().getPath();
  }

  public String getSupportPath() {
    return getSfInstance().getSupport();
  }

  public String getWorkspacePath() {
    String worksapcePath = workspace.getRemote();
    LOGGER.info("Workspace is [" + worksapcePath + "]");
    return worksapcePath;
  }

  public String getSlaveWorkspacePath() {
    if (sfInstance.getSlaveLocalWorkspace() == null || sfInstance.getSlaveLocalWorkspace().isEmpty()) {
      return getWorkspacePath();
    }
    return sfInstance.getSlaveLocalWorkspace();
  }

  public String getSlaveSupportPath() {
    String slaveSupportPath = getSfInstance().getSlaveSupport();
    if (slaveSupportPath == null || slaveSupportPath.isEmpty()) {
      slaveSupportPath = getSupportPath();
    }
    return slaveSupportPath;
  }

  public String getSlaveRunScript() {
    return getSlaveSupportPath() + "/" + getPlatform() + "/runSFSlave." + getPlatform().extension;
  }

  public String getSlaveStopScript() {
    return getSlaveSupportPath() + "/" + getPlatform() + "/stopSFSlave." + getPlatform().extension;
  }

  public String getSlaveDeployScript() {
    return getSlaveSupportPath() + "/" + getPlatform() + "/deploySFSlave." + getPlatform().extension;
  }

  public String getSlaveKillScript() {
    return getSlaveSupportPath() + "/" + getPlatform() + "/clearSFSlave." + getPlatform().extension;
  }

  public String getRunScript() {
    return getSupportPath() + "/" + getPlatform() + "/runSF.sh";
  }

  public String getStopScript() {
    return getSupportPath() + "/" + getPlatform() + "/stopSF.sh";
  }

  public String getDeployScript() {
    return getSupportPath() + "/" + getPlatform() + "/deploySF.sh";
  }

  public String getKillScript() {
    return getSupportPath() + "/" + getPlatform() + "/clearSF.sh";
  }

  protected SmartFrogHost.Platform getPlatform() {
    return host.getPlatform();
  }

  public String applyRewriteRules(String path, List<RewriteRule.Classification> classifications) {
    List<RewriteRule> rewriteRules = getSfInstance().getRewriteRules();
    for (RewriteRule rule : rewriteRules) {
      if (classifications.contains(rule.getClassification())) {
        path = rule.apply(path, getRewriteVariableMap());
      }
    }
    return path;
  }

  protected Map<String, String> getRewriteVariableMap() {
    Map<String, String> variables = new HashMap<String, String>();
    String workspace = this.workspace.getParent().getRemote();
    variables.put("{WORKSPACE}", workspace);
    return variables;
  }

  public abstract String[] buildDaemonCommandLine();

  public abstract String[] buildStopDaemonCommandLine();

  public abstract String[] buildDeployCommandLine(String scriptPath, String componentName);

  public abstract String[] buildDeployTerminateHookCommandLine();

  public abstract String[] buildClearSlaveCommandLine();

  public abstract String getSFClassPath();

  public abstract String getIniPath();

  public abstract String exportMatrixAxes();

  public abstract String applyRewriteRules(String path);
}
