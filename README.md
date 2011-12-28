# AndroidAnnotations plugin for Gradle

This plugin configures a [Gradle](http://gradle.org) project to add [AndroidAnnotations](http://androidannotations.org) to the build process.

## Installation

```
buildscript {
    repositories {
        mavenCentral()
        maven {
            url 'http://maven.teamcodeflux.com/external'
        }
    }

    def gradleAndroidAnnotationsPluginVersion = '0.1.0'

    dependencies {
        classpath "org.androidannotations.gradle.plugins:gradle-androidannotations-plugin:$gradleAndroidAnnotationsPluginVersion"
    }
}

apply plugin: 'androidannotations'
apply plugin: 'idea'

repositories {
    mavenCentral()
}

androidAnnotationsVersion = '2.2'

dependencies {
    compile "com.googlecode.androidannotations:androidannotations:${androidAnnotationsVersion}"
}
```

