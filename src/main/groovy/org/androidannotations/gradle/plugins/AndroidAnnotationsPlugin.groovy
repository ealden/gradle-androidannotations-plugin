/*
 * Copyright 2011 Ealden Esto E. Escanan <ealden@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.androidannotations.gradle.plugins

import com.jvoegele.gradle.plugins.android.AndroidPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

class AndroidAnnotationsPlugin implements Plugin<Project> {
  private project
  private androidAnnotationsConvention

  void apply(Project project) {
    this.project = project

    configurePluginConvention()
    configureBuildScript()
  }

  private void configurePluginConvention() {
    this.androidAnnotationsConvention = new AndroidAnnotationsConvention()
    project.convention.plugins.androidannotations = this.androidAnnotationsConvention
  }

  private void configureBuildScript() {
    project.plugins.apply(AndroidPlugin.class)

    project.repositories {
	  mavenCentral()
      
	  maven {
	  	url 'https://oss.sonatype.org/content/repositories/snapshots/'
	  }	
	}

    project.configurations {
      androidannotations
    }

    project.gradle.taskGraph.whenReady { taskGraph ->
      configureDependencies()
      configurePlugins()
    }
  }

  private void configureDependencies() {
    project.dependencies {
      compile "${androidAnnotationsConvention.androidAnnotationsPackage}:androidannotations-api:${androidAnnotationsConvention.androidAnnotationsVersion}"
      androidannotations "${androidAnnotationsConvention.androidAnnotationsPackage}:androidannotations:${androidAnnotationsConvention.androidAnnotationsVersion}"
    }
  }

  private void configurePlugins() {
    configureJavaPlugin()

    if (project.plugins.hasPlugin('idea')) {
      configureIdeaPlugin()
    }
  }

  private void configureJavaPlugin() {
    project.compileJava {
      doFirst {
        def destinationDir = project.tasks.jar.destinationDir
        project.mkdir destinationDir
        Map otherArgs = [
        includeAntRuntime: false,
        destdir: destinationDir,
        classpath: project.configurations.compile.asPath,
        sourcepath: '',
        target: project.targetCompatibility,
        source: project.sourceCompatibility
        ]
        options.compilerArgs = [
          '-processor', "${androidAnnotationsConvention.androidAnnotationsPackage}.AndroidAnnotationProcessor",
          '-processorpath', project.configurations.androidannotations.asPath,
          '-s', "${destinationDir.absolutePath}".toString(),
          '-classpath', project.configurations.compile.asPath
        ]
        Map antOptions = otherArgs + options.optionMap()
        project.ant.javac(antOptions) {
          source.addToAntBuilder(project.ant, 'src', FileCollection.AntType.MatchingTask)
          options.compilerArgs.each { value ->
            compilerarg(value: value)
          }
        }
      }
    }
  }

  private void configureIdeaPlugin() {
    project.idea.module {
      scopes.PROVIDED.plus += project.configurations.androidannotations
    }

    project.idea.project.ipr.withXml { provider ->
      def compilerConfiguration = provider.node.component.find {
        it.@name == 'CompilerConfiguration'
      }

      def annotationProcessing = compilerConfiguration.annotationProcessing[0]
      annotationProcessing.@enabled = true
      annotationProcessing.@useClasspath = true
      annotationProcessing.appendNode(
        'processor', [
        name: "${androidAnnotationsConvention.androidAnnotationsPackage}.AndroidAnnotationProcessor",
        options: ''
        ])
      annotationProcessing.appendNode(
        'processModule', [
        name: project.name,
        generatedDirName: 'gen'
        ])
    }
  }
}
