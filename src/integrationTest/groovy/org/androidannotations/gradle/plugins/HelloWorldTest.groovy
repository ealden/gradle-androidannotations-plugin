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

import org.androidannotations.gradle.plugins.support.AbstractIntegrationTest
import org.junit.Before
import org.junit.Test

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

class HelloWorldTest extends AbstractIntegrationTest {
    private project

    @Before
    void setUp() {
        this.project = project('HelloWorld')
    }

    @Test
    void assemble() {
        project.runTasks 'clean', 'assemble'

        project.fileExists 'build/libs/HelloWorld-1.0.jar'
    }

    @Test
    void idea() {
        project.runTasks 'cleanIdea', 'idea'

        project.fileExists 'HelloWorld.ipr'
        project.fileExists 'HelloWorld.iws'
        project.fileExists 'HelloWorld.iml'

        def ipr = new XmlSlurper().parse(project.file('HelloWorld.ipr'))

        def compilerConfiguration = ipr.component.find {
            it.@name == 'CompilerConfiguration'
        }

        def annotationProcessing = compilerConfiguration.annotationProcessing

        assertThat(annotationProcessing.@enabled.text(), equalTo('true'))
        assertThat(annotationProcessing.@useClasspath.text(), equalTo('true'))

        assertThat(annotationProcessing.processor.@name.text(), equalTo('com.googlecode.androidannotations.AndroidAnnotationProcessor'))
        assertThat(annotationProcessing.processor.@options.text(), equalTo(''))
        assertThat(annotationProcessing.processModule.@name.text(), equalTo('HelloWorld'))
        assertThat(annotationProcessing.processModule.@generatedDirName.text(), equalTo('gen'))
    }
}
