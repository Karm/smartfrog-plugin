package builder.smartfrog;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;

import java.util.Map;
import java.util.logging.Logger;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author jcechace
 */
public class RewriteRule extends AbstractDescribableImpl<RewriteRule> {
  private String sequence;
  private String replacement;
  private Classification classification;
  private static final Logger LOGGER = Logger.getLogger(RewriteRule.class.getName());

  public static enum Classification {
    COMMON, LINUX, WINDOWS;

    @Override
    public String toString() {
      return super.toString();
    }
  }

  @DataBoundConstructor
  public RewriteRule(String sequence, String replacement, Classification classification) {
    this.sequence = sequence;
    this.replacement = replacement;
    this.classification = classification;
  }

  public String apply(String path) {
    return _apply(sequence, replacement, path);
  }

  public String apply(String path, Map<String, String> variables) {
    String expandedSequence = sequence;
    for (String var : variables.keySet()) {
      LOGGER.info("Rewriting [" + var + "] with [" + variables.get(var) + "].");
      expandedSequence = expandedSequence.replace(var, variables.get(var));
    }
    return _apply(expandedSequence, replacement, path);
  }

  private String _apply(String sequence, String replacement, String path) {
    if (!path.startsWith(sequence)) {
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

  public Classification getClassification() {
    return classification;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<RewriteRule> {
    public String getDisplayName() {
      return "";
    }

    public ListBoxModel doFillClassificationItems() {
      ListBoxModel lb = new ListBoxModel();
      for (Classification c : Classification.values()) {
        lb.add(c.toString(), c.toString());
      }
      return lb;
    }
  }

}
