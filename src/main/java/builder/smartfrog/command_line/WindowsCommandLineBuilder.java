package builder.smartfrog.command_line;

import builder.smartfrog.RewriteRule;
import builder.smartfrog.SmartFrogHost;
import builder.smartfrog.SmartFrogInstance;
import builder.smartfrog.util.Functions;

import java.util.ArrayList;
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
        String initPath = getBuilder().isUseAltIni() ?
                getBuilder().getSfIni() : getSfInstance().getPath() + "/bin/default.ini";

        return applyRewriteRules(initPath);
    }

    @Override
    public String getSlaveSupportPath() {
        String slaveSupportPath = super.getSlaveSupportPath()
                .replace("\\","/");
        return applyRewriteRules(slaveSupportPath);
    }

    @Override
    public String getSfInstancePath() {
        String sfInstancePath = super.getSfInstancePath()
                .replace("\\", "/");
        return applyRewriteRules(sfInstancePath);
    }

    @Override
    public String getWorkspacePath() {
        String workspacePath = applyRewriteRules(super.getWorkspacePath())
                .replace("\\", "/");
        return applyRewriteRules(workspacePath);
    }



    @Override
    public String exportMatrixAxes() {
        return "";
    }

    @Override
    public String getJdk() {
        String jdk = super.getJdk();
        return applyRewriteRules(jdk);
    }



    @Override
    public String[] buildDaemonCommandLine() {
        String hostName =  getHost().getName();
        return new String[] {"bash", "-xe", getRunScript(), hostName, getSlaveRunScript(),
                getSfInstancePath(), getSFClassPath(), getWorkspacePath(), getSlaveWorkspacePath(),
                getJdk()};
    }

    @Override
    public String[] buildDeployCommandLine(String scriptPath, String componentName) {
        scriptPath = applyRewriteRules(scriptPath);
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
    public String[] buildDeployTerminateHookCommandLine() {
        String path = getSlaveSupportPath() + "/" + SmartFrogInstance.SUPPORT_SCRIPT;
        return  buildDeployCommandLine(path, "terminate-hook");
    }

    @Override
    public String[] buildStopDaemonCommandLine() {
        String hostName =  getHost().getName();
        return new String[] {"bash", "-xe", getStopScript(), hostName, getSlaveStopScript(),
                getSfInstancePath(), getSFClassPath(), getWorkspacePath(), getSlaveWorkspacePath(),
                getJdk()};
    }

    @Override
    public  String[] buildClearSlaveCommandLine() {
        String hostName =  getHost().getName();
        return new String[] { "bash", "-xe", getKillScript(), getSlaveKillScript(), hostName };
    }

    @Override
    public String applyRewriteRules(String path) {
        List<RewriteRule.Classification> classifications =
                new ArrayList<RewriteRule.Classification>();
        classifications.add(RewriteRule.Classification.COMMON);
        classifications.add(RewriteRule.Classification.WINDOWS);
        return super.applyRewriteRules(path, classifications);
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
