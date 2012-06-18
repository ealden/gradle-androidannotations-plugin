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

import org.gradle.testfixtures.ProjectBuilder

abstract class AbstractIntegrationTest {
  def testProjectsDir = new File(System.getProperty('integrationTest.testProjects'))

  def project(path) {
    def projectDir = new File(testProjectsDir, path)
    def project = ProjectBuilder.builder().withProjectDir(projectDir).build()

    return new TestProject(project: project)
  }
}
