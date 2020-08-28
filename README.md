# secure-post-script

[![Build Status](https://ci.jenkins.io/job/Plugins/job/secure-post-script-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/secure-post-script-plugin/job/master/)
[![Contributors](https://img.shields.io/github/contributors/jenkinsci/secure-post-script-plugin.svg)](https://github.com/jenkinsci/secure-post-script-plugin/graphs/contributors)
[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/secure-post-script.svg)](https://plugins.jenkins.io/secure-post-script)
[![GitHub release](https://img.shields.io/github/release/jenkinsci/secure-post-script-plugin.svg?label=changelog)](https://github.com/jenkinsci/secure-post-script-plugin/releases/latest)
[![Jenkins Plugin Installs](https://img.shields.io/jenkins/plugin/i/secure-post-script.svg?color=blue)](https://plugins.jenkins.io/secure-post-script)

## Introduction

This plugin allows you to configure a global groovy script with assistance `secure-script` plugins, and at which condition the script will be executed. 
![secure post script configuration ](/docs/secure-post-script.png)

### Variables could be used in groovy script
    - All Jenkins Environment Variables are availabl(`printenv`).
    - `out.println()` could be used to print out information on build log.


## Getting started
sample code
```groovy
out.println("Job: $JOB_NAME build number: $BUILD_NUMBER has been built on $NODE_NAME ")
```

## Contributing

Refer to our [contribution guidelines](https://github.com/jenkinsci/.github/blob/master/CONTRIBUTING.md)

## LICENSE

Licensed under MIT, see [LICENSE](LICENSE.md)

