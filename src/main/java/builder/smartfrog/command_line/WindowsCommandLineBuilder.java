package builder.smartfrog.command_line;

import builder.smartfrog.RewriteRule;
import builder.smartfrog.SmartFrogHost;
import builder.smartfrog.SmartFrogInstance;
import builder.smartfrog.util.Functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jcechace
 */
public class WindowsCommandLineBuilder extends AbstractCommandLineBuilder implements CommandLineBuilder {

    public WindowsCommandLineBuilder(SmartFrogHost host) {
        super(host);
    }

    @Override
    public String getIniPath() {
        return getBuilder().isUseAltIni() ?
                getBuilder().getSfIni() : getSfInstance().getPath() + "/bin/default.ini";
    }

    @Override
    public String getSlaveSupportPath() {
        return super.getSlaveSupportPath()
                .replace("\\","/");
    }

    @Override
    public String getSfInstancePath() {
        return super.getSfInstancePath()
                .replace("\\", "/");
    }

    @Override
    public String getWorkspacePath() {
        return applyRewriteRules(super.getWorkspacePath())
                .replace("\\", "/");
    }

    @Override
    public String exportMatrixAxes() {
        return "";
    }

    @Override
    public String[] buildDaemonCommandLine() {
        String hostName =  getHost().getName();
        return new String[] {"bash", "-xe", getRunScript(), hostName, getSlaveRunScript(),
                getSfInstancePath(), getSFClassPath(), getWorkspacePath(), getSlaveWorkspacePath()};
    }

    @Override
    public String[] buildDeployCommandLine(String scriptPath, String componentName) {
        // TODO: path transformation workaround
        if (getWorkspacePath() == getSlaveWorkspacePath()) {
            scriptPath = scriptPath.replaceFirst(super.getWorkspacePath(), getWorkspacePath());
        } else {
            scriptPath = scriptPath.replaceFirst(super.getWorkspacePath(), getSlaveWorkspacePath());
        }

        String hostName =  getHost().getName();
        return new String[] {"bash", "-xe", getDeployScript(), hostName,  getSlaveDeployScript(),
                getSfInstancePath(), getSFClassPath(), componentName, scriptPath,
                getWorkspacePath(), getSlaveWorkspacePath()};
    }

    @Override
    public String[] buildDeployTerminateHookCommandLine() {
        String path = getSlaveSupportPath() + "/" + SmartFrogInstance.SUPPORT_SCRIPT;
        return  buildDeployCommandLine(path, "terminate-hook");
    }

    @Override
    public String[] buildStopDaemonCommandLine() {
        String hostName =  getHost().getName();
        return new String[] {"bash", "-xe", getStopScript(), hostName, getSlaveStopScript(),
                getSfInstancePath(), getSFClassPath(), getWorkspacePath(), getSlaveWorkspacePath()};
    }

    @Override
    public  String[] buildKillThemAllCommandLine() {
        String hostName =  getHost().getName();
        return new String[] { "bash", "-xe", getKillScript(), getSlaveKillScript(), hostName};
    }

    @Override
    public String applyRewriteRules(String path) {
        List<RewriteRule> rewriteRules = getSfInstance().getRewriteRules();
        for (RewriteRule rule : rewriteRules) {
                path = rule.apply(path, getRewriteVariableMap());
        }
        return path;
    }


    private Map<String, String> getRewriteVariableMap() {
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("{WORKSPACE}", super.getWorkspacePath());

        return variables;
    }


    public String  getSFClassPath() {
        String supportPath = getSlaveSupportPath() + "/*;";
        StringBuilder sfPath = new StringBuilder(supportPath);
        String[] userHomes = getBuilder().getSfUSerHomes();
        for (String home : userHomes) {
            if (!home.isEmpty()){
                sfPath.append(home + "/*").append(";");
            }
        }
        // TODO: workaround. Space will ensure that parameter containing semicolon will be passed correctly to batch script
        sfPath.append(" ");
        return sfPath.toString();
    }
}
