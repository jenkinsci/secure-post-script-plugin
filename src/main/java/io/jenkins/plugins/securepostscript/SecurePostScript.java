package io.jenkins.plugins.securepostscript;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.*;
import hudson.model.listeners.RunListener;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;
import org.jenkinsci.plugins.scriptsecurity.scripts.ApprovalContext;

@Extension
public class SecurePostScript extends RunListener<Run<?, ?>> {

  @Override
  public void onCompleted(final Run run, final TaskListener listener) {
    final EnvVars envVars = getEnvVars(run, listener);
    SecurePostScriptConfiguration cfg = SecurePostScriptConfiguration.get();
    System.out.println("result: " + cfg.getRunCondition());
    if (run.getResult().isWorseThan(SecurePostScriptConfiguration.get().getResultCondition())) {
      return;
    }

    final SecureGroovyScript script = SecurePostScriptConfiguration.get().getSecureGroovyScript();
    System.out.println("script to be executed: " + script.getScript());
    try {
      script.configuring(ApprovalContext.create().withCurrentUser());
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
}
