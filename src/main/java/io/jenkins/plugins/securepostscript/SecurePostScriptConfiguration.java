package io.jenkins.plugins.securepostscript;

import hudson.Extension;
import hudson.model.Result;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;

import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.CheckForNull;

@Extension
public class SecurePostScriptConfiguration extends GlobalConfiguration {

  private SecureGroovyScript secureGroovyScript;
  
  private Result runCondition = Result.SUCCESS;

  public static SecurePostScriptConfiguration get() {
    return jenkins.model.GlobalConfiguration.all().get(SecurePostScriptConfiguration.class);
  }

  public SecurePostScriptConfiguration() {
    load();
  }

  @CheckForNull
  public SecureGroovyScript getSecureGroovyScript() {
    return secureGroovyScript;
  }

  /**
   * Together with {@link #getSecureGroovyScript}, binds to entry in
   * {@code config.jelly}.
   *
   * @param secureGroovyScript the new value of this field
   */
  @DataBoundSetter
  public void setSecureGroovyScript(SecureGroovyScript secureGroovyScript) {
    this.secureGroovyScript = secureGroovyScript;
    save();
  }
  
  public Result getResultCondition() {
    return runCondition;
  }

  public String getRunCondition() {
    return runCondition.toString();
  }

  @Override
  public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
    this.runCondition = Result.fromString(json.getString("runCondition"));
    save();
    return super.configure(req, json);
  }

  public FormValidation doCheckScriptContent(@QueryParameter String value) {
    return FormValidation.ok();
  }

}