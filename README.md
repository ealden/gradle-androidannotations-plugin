# AndroidAnnotations plugin for Gradle

This plugin configures a [Gradle](http://gradle.org) project to add [AndroidAnnotations](http://androidannotations.org) to the build process.

## Installation

```
buildscript {
    repositories {
        mavenCentral()
    }

    def gradleAndroidAnnotationsPluginVersion = '1.2.2-SNAPSHOT'

    dependencies {
        classpath "net.ealden.gradle.plugins:gradle-androidannotations-plugin:$gradleAndroidAnnotationsPluginVersion"
    }
}

apply plugin: 'androidannotations'
apply plugin: 'idea'

androidAnnotationsVersion = '3.0-SNAPSHOT'
```

[![Built on DEV@cloud](http://web-static-cloudfront.s3.amazonaws.com/images/badges/BuiltOnDEV.png)](https://gradle-androidannotations-plugin.ci.cloudbees.com)

