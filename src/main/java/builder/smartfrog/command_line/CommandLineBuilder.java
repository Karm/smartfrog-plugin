package builder.smartfrog.command_line;

/**
 * @author jcechace
 */
public interface CommandLineBuilder {
    String[] buildDaemonCommandLine();
    String[] buildStopDaemonCommandLine();
    String[] buildDeployCommandLine(String scriptPath, String componentName);
    String[] buildDeployTerminateHookCommandLine();
    String[] buildKillThemAllCommandLine();
}
