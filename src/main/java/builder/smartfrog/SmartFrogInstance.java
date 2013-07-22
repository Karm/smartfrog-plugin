/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package builder.smartfrog;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an installation object - name of the SmartFrog environment installation
 * and the path where it's located.
 *
 * @author Dominik Pospisil
 * @author <a href="http://www.radoslavhusar.com/">Radoslav Husar</a>
 * @author vjuranek
 * @author jcechace
 */
public class SmartFrogInstance extends AbstractDescribableImpl<SmartFrogInstance> {

    public static final String SUPPORT_SCRIPT = "hudson-support.sf";

    private String name;
    private String path;
    private String support;
    private String slaveSupport;
    private String slaveLocalWorkspace;
    private String deployScript;
    private String runScript;
    private String stopScript;
    private String killScript;
    private List<RewriteRule> rewriteRules;

    protected Object readResolve() {
        if (rewriteRules == null) {
            rewriteRules = new ArrayList<RewriteRule>();
        }
        return this;
    }

    @DataBoundConstructor
    public SmartFrogInstance(String name, String path, String support, String slaveSupport,
                             String slaveLocalWorkspace, List<RewriteRule> rewriteRules,
                             String deployScript, String runScript, String stopScript,
                             String killScript) {
        this.name = name;
        this.path = path;
        this.support = support;
        this.slaveSupport = slaveSupport;
        this.slaveLocalWorkspace = slaveLocalWorkspace;
        this.deployScript = deployScript;
        this.runScript = runScript;
        this.stopScript = stopScript;
        this.killScript = killScript;
        this.rewriteRules = rewriteRules;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    /**
     * @return path to support files needed for SmartFrog (sf, jar, sh).
     */
    public String getSupport() {
        return support;
    }

    public String getSlaveSupport() {
        return slaveSupport;
    }

    public String getSlaveLocalWorkspace() {
        return slaveLocalWorkspace;
    }

    public String getDeployScript() {
        return deployScript;
    }

    public String getRunScript() {
        return runScript;
    }

    public String getStopScript() {
        return stopScript;
    }

    public String getKillScript() {
        return killScript;
    }

    public List<RewriteRule> getRewriteRules() {
        return rewriteRules;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<SmartFrogInstance> {
        public String getDisplayName() {
            return "";
        }
    }

}
