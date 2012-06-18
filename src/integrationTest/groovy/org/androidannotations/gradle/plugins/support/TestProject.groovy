/*
 * Copyright 2011 the original author or authors.
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

package org.androidannotations.gradle.plugins.support

import org.gradle.GradleLauncher
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem

import static org.junit.Assert.*

class TestProject {
  Project project

  def runTasks(String... tasks) {
    runTasks([:], tasks as List<String>)
  }

  def runTasks(Map<String, Object> args, String... tasks) {
    runTasks(args, tasks as List<String>)
  }

  def runTasks(Map<String, Object> args, List<String> tasks) {
    if (OperatingSystem.current().isWindows()) {
      tasks.remove 'clean'
    }

    def startParameter = project.gradle.startParameter.newBuild()
    startParameter.projectDir = project.projectDir

    if (args.buildScript) {
      startParameter.buildFile = new File(project.projectDir, args.buildScript)
    }

    startParameter.taskNames = tasks

    def launcher = GradleLauncher.newInstance(startParameter)
    launcher.addListener(new IntegrationTestBuildListener())

    def result = launcher.run()
    result.rethrowFailure()
  }

  def file(path) {
    project.file(path)
  }

  def exec(closure) {
    project.exec(closure)
  }

  def fileExists(path) {
    assertTrue("File ${path} must exist", file(path).isFile())
  }

  def fileDoesntExist(path) {
    if (OperatingSystem.current().isWindows()) {
      return
    }

    assertFalse("File ${path} must not exist", file(path).exists())
  }
}
