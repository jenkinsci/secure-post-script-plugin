package io.jenkins.plugins.securepostscript;

import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;
import groovy.lang.Binding;
import hudson.model.TaskListener;
import jenkins.model.Jenkins;
import java.util.Map;

public class GroovyScriptRunner {

  // Run the groovy script wrapped in SecureGroovyScript.
  public void run(SecureGroovyScript script, Map<String, String> variables, TaskListener listener) {
    Binding bindings = new Binding();
    for (Map.Entry<String, String> entry : variables.entrySet()) {
      bindings.setVariable(entry.getKey(), entry.getValue());
    }
    // append `out` to groovy script execution context, which could be used to
    // print out information on build log.
    bindings.setVariable("out", listener.getLogger());
    try {
      ClassLoader loader = Jenkins.get().getPluginManager().uberClassLoader;
      script.evaluate(loader, bindings, listener);
    } catch (Throwable e) {
      e.printStackTrace();
      println(listener, "Failed to execute groovy script configured by `secure post script` plugin.\n" +  e.getMessage());
    }
  }

  protected void println(TaskListener listener, String message) {
    listener.getLogger().println(message);
  }
}
