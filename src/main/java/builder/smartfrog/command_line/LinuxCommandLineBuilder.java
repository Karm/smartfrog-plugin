package builder.smartfrog.command_line;

import builder.smartfrog.SmartFrogBuilder;
import builder.smartfrog.SmartFrogInstance;
import hudson.matrix.Combination;
import hudson.matrix.MatrixConfiguration;
import hudson.model.AbstractBuild;

import java.util.Map;

import builder.smartfrog.SmartFrogHost;

/**
 * @author jcechace
 */
public class LinuxCommandLineBuilder extends AbstractCommandLineBuilder {

    public LinuxCommandLineBuilder(SmartFrogHost host) {
        super(host);
    }
    public  LinuxCommandLineBuilder(){

    }

    @Override
    public String  getIniPath() {
        return getBuilder().isUseAltIni() ? getBuilder().getSfIni() : getSfInstance().getPath() + "/bin/default.ini";
    }

    @Override
    public String getSlaveWorkspacePath() {
        return getWorkspacePath();
    }

    @Override
    public String getRunScript() {
        return getSupportPath() + "/runSF.sh";
    }

    @Override
    public String getStopScript() {
        return  getSupportPath() + "/stopSF.sh";
    }

    @Override
    public String getDeployScript() {
        return getSupportPath() + "/deploySF.sh";
    }

    @Override
    public String exportMatrixAxes(){
        String exportedMatrixAxes = " ";

        AbstractBuild<?, ?> build = getHost().getSfAction().getOwnerBuild();

        if (build.getProject() instanceof MatrixConfiguration){
            MatrixConfiguration matrix = (MatrixConfiguration) build.getProject();
            Combination combinations = matrix.getCombination();
            // Add only "SF_" prefixed variables.
            for (Map.Entry<String, String> entry : combinations.entrySet()) {
                if (entry.getKey().startsWith("SF_")) {
                    exportedMatrixAxes = exportedMatrixAxes + "export " + entry.getKey() + "=" + entry.getValue() + "; ";
                }
            }
        }
        return exportedMatrixAxes;
    }

    @Override
    public String[] buildDaemonCommandLine() {
        // TODO: fix SfUSerHomeX
        SmartFrogBuilder builder = getBuilder();
        return new String[] { "bash", "-xe", getRunScript(),  getHost().getName(), getSfInstancePath(),
                builder.getSfUserHome(), getSupportPath(), builder.getSfUserHome2(), builder.getSfUserHome3(),
                builder.getSfUserHome4(), getWorkspacePath(), getSfOpts(), getIniPath(), exportMatrixAxes(), getJdk()};
    }

    @Override
    public String[] buildStopDaemonCommandLine() {
        return new String[] { "bash", "-xe", getStopScript(), getHost().getName(), getSfInstancePath(),
                getBuilder().getSfUserHome(), getJdk()};
    }

    @Override
    public String[] buildDeployCommandLine(String scriptPath, String componentName) {
        // TODO: fix SfUSerHomeX
        SmartFrogBuilder builder = getBuilder();
        return new String[] { "bash", "-xe", getDeployScript(), getHost().getName(), getSfInstancePath(),
                builder.getSfUserHome(), getSupportPath(), builder.getSfUserHome2(), builder.getSfUserHome3(),
                builder.getSfUserHome4(), scriptPath, componentName, getWorkspacePath(), exportMatrixAxes(), getJdk()};
    }

    @Override
    public String[] buildDeployTerminateHookCommandLine() {
        String path = getSlaveSupportPath() + "/" + SmartFrogInstance.SUPPORT_SCRIPT;
        return  buildDeployCommandLine(path, "terminate-hook");
    }

    public  String[] buildKillThemAllCommandLine() {
        String hostName =  getHost().getName();
        return new String[] { "bash", "-xe", getKillScript(), hostName};
    }

    @Override
    public String applyRewriteRules(String path) {
        throw new UnsupportedOperationException("Rewrite rules are not supported yet for Linux commandline builder");
    }
}
