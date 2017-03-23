package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.common.assertThrows
import com.elpassion.mainframerplugin.task.MFTaskData
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.openapi.project.Project
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class MFRunConfigurationTest {

    private val confFactory = MFConfigurationFactory(MFRunConfigurationType())
    private val project = mock<Project>()

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenBuildCommandIsBlankOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Build command cannot be empty",
                taskData = mfTaskData(buildCommand = ""))
    }

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenScriptPathIsInvalidOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Mainframer tool cannot be found",
                taskData = mfTaskData(mainframerPath = ""))
    }

    @Test
    fun shouldReturnFalseOnIsCompileBeforeLaunchAddedByDefault() {
        assertFalse(mfRunConfiguration().isCompileBeforeLaunchAddedByDefault)
    }

    private fun assertExceptionMessageOnCheckConfiguration(expectedMessage: String, taskData: MFTaskData?) {
        val exception = assertThrows<RuntimeConfigurationError> {
            mfRunConfiguration()
                    .apply { data = taskData }
                    .checkConfiguration()
        }
        assertEquals(expectedMessage, exception.message)
    }

    private fun mfRunConfiguration() = MFRunConfiguration(project, confFactory, "")

    private fun mfTaskData(buildCommand: String = "buildCommand",
                           mainframerPath: String = "path") = MFTaskData(mainframerPath = mainframerPath, buildCommand = buildCommand)
}