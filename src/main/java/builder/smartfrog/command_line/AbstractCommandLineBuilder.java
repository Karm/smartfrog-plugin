package builder.smartfrog.command_line;

import hudson.FilePath;
import hudson.model.JDK;
import builder.smartfrog.SmartFrogBuilder;
import builder.smartfrog.SmartFrogHost;
import builder.smartfrog.SmartFrogInstance;
import builder.smartfrog.util.Functions;

/**
 * @author jcechace
 */
public abstract class AbstractCommandLineBuilder implements CommandLineBuilder{
    public static final String DEF_JDK = "$JAVA_HOME";

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
        // TODO: this can be improved by using getHost().getSfAction().getOwnerBuild().getEnvironment()
        JDK jdk = getHost().getSfAction().getOwnerBuild().getProject().getJDK();
        String path =  jdk == null ?  DEF_JDK : jdk.getHome();

        if (host.getPlatform() != SmartFrogHost.Platform.WINDOWS) {
            // TODO: Hack to prevent jenkins from expanding env variables
            path = path.replace("$", "@");
        }

        return host.getJdk() != null ? host.getJdk() : path;
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
        if (sfInstance.getSlaveLocalWorkspace() == null || sfInstance.getSlaveLocalWorkspace() == "") {
            return getWorkspacePath();
        }
        return sfInstance.getSlaveLocalWorkspace();
    }

    public String getSlaveSupportPath() {
        return getSfInstance().getSlaveSupport();
    }

    public String getSlaveRunScript() {
        return sfInstance.getRunScript();
    }

    public String getSlaveStopScript() {
        return sfInstance.getStopScript();
    }

    public String getSlaveDeployScript() {
        return sfInstance.getDeployScript();
    }

    public String getSlaveKillScript() {
        return sfInstance.getKillScript();
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

    protected String getPlatform() {
        return host.getPlatform().toString().toLowerCase();
    }

    public abstract String[] buildDaemonCommandLine();
    public abstract String[] buildStopDaemonCommandLine();
    public abstract String[] buildDeployCommandLine(String scriptPath, String componentName);
    public abstract String[] buildDeployTerminateHookCommandLine();
    public abstract String[] buildKillThemAllCommandLine();

    public abstract String getIniPath();
    public abstract String exportMatrixAxes();
    public abstract String applyRewriteRules(String path);
}
