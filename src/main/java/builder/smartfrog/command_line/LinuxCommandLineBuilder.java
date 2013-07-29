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

    @Override
    public String  getIniPath() {
        String initPath = getBuilder().isUseAltIni() ?
                getBuilder().getSfIni() : getSfInstance().getPath() + "/bin/default.ini";
        return initPath;
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
        String hostName =  getHost().getName();
        return new String[] {"bash", "-xe", getRunScript(), hostName, getSlaveRunScript(),
                getSfInstancePath(), getSFClassPath(), getWorkspacePath(), getSlaveWorkspacePath(),
                getJdk(), getSfOpts(), getIniPath()};
    }

    @Override
    public String[] buildDeployCommandLine(String scriptPath, String componentName) {
        if (getWorkspacePath().equals(getSlaveWorkspacePath())) {
            scriptPath = scriptPath.replaceFirst(super.getWorkspacePath(), getWorkspacePath());
        } else {
            scriptPath = scriptPath.replaceFirst(super.getWorkspacePath(), getSlaveWorkspacePath());
        }

        String hostName =  getHost().getName();
        return new String[] {"bash", "-xe", getDeployScript(), hostName,  getSlaveDeployScript(),
                getSfInstancePath(), getSFClassPath(), componentName, scriptPath,
                getWorkspacePath(), getSlaveWorkspacePath(), getJdk()};
    }

    @Override
    public String[] buildStopDaemonCommandLine() {
        String hostName =  getHost().getName();
        return new String[] {"bash", "-xe", getStopScript(), hostName, getSlaveStopScript(),
                getSfInstancePath(), getSFClassPath(), getWorkspacePath(), getSlaveWorkspacePath(),
                getJdk()};
    }

    @Override
    public String[] buildDeployTerminateHookCommandLine() {
        String path = getSlaveSupportPath() + "/" + SmartFrogInstance.SUPPORT_SCRIPT;
        return  buildDeployCommandLine(path, "terminate-hook");
    }

    @Override
    public  String[] buildClearSlaveCommandLine() {
        String hostName =  getHost().getName();
        return new String[] { "bash", "-xe", getKillScript(), getSlaveKillScript(), hostName};
    }

    @Override
    public String applyRewriteRules(String path) {
        throw new UnsupportedOperationException("Rewrite rules are not supported yet for Linux commandline builder");
    }

    public String  getSFClassPath() {
        String supportPath = getSlaveSupportPath() + "/*:";
        StringBuilder sfPath = new StringBuilder(supportPath);
        String[] userHomes = getBuilder().getSfUSerHomes();
        for (String home : userHomes) {
            if (!home.isEmpty()){
                sfPath.append(home + "/*").append(":");
            }
        }
        return sfPath.toString();
    }
}

