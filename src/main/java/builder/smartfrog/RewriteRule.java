package builder.smartfrog;


import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Map;

/**
 * @author jcechace
 */
public class RewriteRule extends AbstractDescribableImpl<RewriteRule> {
    private String sequence;
    private String replacement;

    @DataBoundConstructor
    public RewriteRule(String sequence, String replacement) {
        this.sequence = sequence;
        this.replacement = replacement;
    }

    public String apply(String path) {
        return _apply(sequence, replacement, path);
    }

    public String apply(String path, Map<String, String> variables) {
        String expandedSequence = sequence;
        for (String var : variables.keySet()) {
            expandedSequence = expandedSequence.replace(var, variables.get(var));
        }
        return _apply(expandedSequence, replacement, path);
    }

    private String _apply(String sequence, String replacement, String path) {
        if (!path.contains(sequence)) {
            return path;
        }
        return path.replaceFirst(sequence, replacement);
    }


    public String getSequence() {
        return sequence;
    }

    public String getReplacement() {
        return replacement;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<RewriteRule> {
        public String getDisplayName() {
            return "";
        }
    }

}
