package io.jenkins.plugins.securepostscript;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.*;
import hudson.model.listeners.RunListener;
import jenkins.model.Jenkins;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;
import org.jenkinsci.plugins.scriptsecurity.scripts.ClasspathEntry;
import org.jenkinsci.plugins.scriptsecurity.scripts.ApprovalContext;
import org.kohsuke.stapler.StaplerRequest;

import java.net.MalformedURLException;
import java.util.List;
import java.util.ArrayList;

@Extension
public class SecurePostScript extends RunListener<Run<?, ?>> implements Describable<SecurePostScript> {

  @Override
  public void onCompleted(final Run run, final TaskListener listener) {
    final EnvVars envVars = getEnvVars(run, listener);

    if (run.getResult().isWorseThan(getDescriptorImpl().getResultCondition())) {
      return;
    }

    final SecureGroovyScript script = getDescriptorImpl().getSecureGroovyScript();
    try {
      new GroovyScriptRunner().run(script, envVars, listener);
    } catch (final Throwable e) {
      e.printStackTrace(listener.getLogger());
    }
  }

  private EnvVars getEnvVars(final Run run, final TaskListener listener) {
    try {
      final EnvVars envVars = run.getEnvironment(listener);
      envVars.put("BUILD_RESULT", run.getResult().toString());
      return envVars;
    } catch (final Throwable e) {
      e.printStackTrace();
      return null;
    }
  }

  public Descriptor<SecurePostScript> getDescriptor() {
    return getDescriptorImpl();
  }

  public DescriptorImpl getDescriptorImpl() {
    return (DescriptorImpl) Jenkins.get().getDescriptorOrDie(SecurePostScript.class);
  }

  @Extension
  public static final class DescriptorImpl extends Descriptor<SecurePostScript> {

    private Result runCondition = Result.UNSTABLE;
    private SecureGroovyScript secureGroovyScript;

    public DescriptorImpl() {
      load();
    }

    public SecureGroovyScript getSecureGroovyScript() {
      return secureGroovyScript;
    }

    public String getDisplayName() {
      return "Secure Post Script";
    }

    @Override
    public boolean configure(final StaplerRequest req, final JSONObject formData) throws Descriptor.FormException {
      final boolean sandbox = formData.getJSONObject("secureGroovyScript").getBoolean("sandbox");
      final String rawScript = formData.getJSONObject("secureGroovyScript").getString("script");
      final JSONArray paths = formData.getJSONObject("secureGroovyScript").optJSONArray("classpath");
      final List<ClasspathEntry> cp = new ArrayList<ClasspathEntry>();
      if (paths != null) {
        for (int i = 0; i < paths.toArray().length; i++) {
          final String path = JSONObject.fromObject(paths.get(i)).getString("path");
          try {
            cp.add(new ClasspathEntry(path));
          } catch (final MalformedURLException e) {
            e.printStackTrace();
          }
        }
      }

      this.secureGroovyScript = new SecureGroovyScript(rawScript, sandbox, cp).configuring(ApprovalContext.create());
      runCondition = Result.fromString(formData.getString("runCondition"));
      save();
      return super.configure(req, formData);
    }

    public Result getResultCondition() {
      return runCondition;
    }

    public String getRunCondition() {
      return runCondition.toString();
    }
  }
}
