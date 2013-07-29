package builder.smartfrog.command_line;

import builder.smartfrog.RewriteRule;
import hudson.FilePath;
import hudson.model.JDK;
import builder.smartfrog.SmartFrogBuilder;
import builder.smartfrog.SmartFrogHost;
import builder.smartfrog.SmartFrogInstance;
import builder.smartfrog.util.Functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jcechace
 */
public abstract class AbstractCommandLineBuilder implements CommandLineBuilder{
    private SmartFrogBuilder builder;
    private SmartFrogInstance sfInstance;
    private SmartFrogHost host;
    private FilePath workspace;




    public AbstractCommandLineBuilder(SmartFrogHost host){
        this.builder = host.getSfAction().getBuilder();
        this.sfInstance = this.builder.getSfInstance();
        this.host = host;
        this.workspace = host.getSfAction().getOwnerBuild().getWorkspace();

    }

    public AbstractCommandLineBuilder(){

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

    public String getSfOpts(){
        return host.getSfOpts() == null ? getBuilder().getSfOpts() : host.getSfOpts();
    }

    public String getJdk(){
        String jdk = host.getJdk();
        if (jdk == null || jdk.isEmpty()) {
            if (host.getPlatform() != SmartFrogHost.Platform.WINDOWS) {
                jdk = "@JAVA_HOME";
            } else {
                jdk = "";
            }
        }
        return jdk;
    }

    public String getSfInstancePath() {
        return getSfInstance().getPath();
    }

    public String getSupportPath() {
        return getSfInstance().getSupport();
    }

    public String getWorkspacePath() {
        return Functions.convertWsToCanonicalPath(workspace);
    }

    public String getSlaveWorkspacePath() {
        if (sfInstance.getSlaveLocalWorkspace() == null || sfInstance.getSlaveLocalWorkspace().isEmpty()) {
            return getWorkspacePath();
        }
        return sfInstance.getSlaveLocalWorkspace();
    }

    public String getSlaveSupportPath() {
        String  slaveSupportPath = getSfInstance().getSlaveSupport();
        if (slaveSupportPath == null || slaveSupportPath.isEmpty()) {
            slaveSupportPath = getSupportPath();
        }
        return slaveSupportPath;
    }

    public String getSlaveRunScript() {
        return getSlaveSupportPath() + "/" + getPlatform() +
                "/runSFSlave." + getPlatform().extension;
    }

    public String getSlaveStopScript() {
        return getSlaveSupportPath() + "/" + getPlatform() +
                "/stopSFSlave." + getPlatform().extension;
    }

    public String getSlaveDeployScript() {
        return getSlaveSupportPath() + "/" + getPlatform() +
                "/deploySFSlave." + getPlatform().extension;
    }

    public String getSlaveKillScript() {
        return getSlaveSupportPath() + "/" + getPlatform() +
                "/clearSFSlave." + getPlatform().extension;
    }

    public String getRunScript() {
        return getSupportPath() + "/" + getPlatform() +  "/runSF.sh";
    }

    public String getStopScript() {
        return  getSupportPath() + "/" + getPlatform() +  "/stopSF.sh";
    }

    public String getDeployScript() {
        return getSupportPath() + "/" + getPlatform() +  "/deploySF.sh";
    }

    public String getKillScript() {
        return getSupportPath() + "/" + getPlatform() +  "/killThemAll.sh";
    }

    protected SmartFrogHost.Platform getPlatform() {
        return host.getPlatform();
    }

    public String applyRewriteRules(String path, List<RewriteRule.Classification> classifications) {
        List<RewriteRule> rewriteRules = getSfInstance().getRewriteRules();
        for (RewriteRule rule : rewriteRules) {
            if (classifications.contains(rule.getClassification())){
                path = rule.apply(path, getRewriteVariableMap());
            }
        }
        return path;
    }

    protected Map<String, String> getRewriteVariableMap() {
        Map<String, String> variables = new HashMap<String, String>();
        String workspace = Functions.convertWsToCanonicalPath(this.workspace.getParent());
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
