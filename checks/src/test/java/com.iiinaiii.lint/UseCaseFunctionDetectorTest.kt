package com.iiinaiii.lint

import com.android.tools.lint.detector.api.Issue
import com.serchinastico.lin.test.LintTest
import com.serchinastico.lin.test.LintTest.Expectation.NoErrors
import com.serchinastico.lin.test.LintTest.Expectation.SomeError

import org.junit.Assert.*
import org.junit.Test

class UseCaseFunctionDetectorTest : LintTest {
    override val issue: Issue
        get() = CheckUseCaseSuspendFunctionDetector.issue

    @Test
    fun whenContainsNotSuspendFunction_detectsErrors() {
        expect(
            // language=kotlin
            """
                package foo

                class SampleUseCase @Inject constructor(
                    private val sampleRepository: SampleRepository
                ) {

                    operator fun invoke(id: Int): Result<SampleData> {
                        val result = sampleRepository.getSampleData(id)
                        return when (result) {
                            is Result.Success -> Result.Success(result.data.toSampleData())
                            is Result.Error -> result
                        }.exhaustive
                    }
                }
            """.inKotlin
        ) toHave SomeError("src/foo/SampleUseCase.kt")
    }

    @Test
    fun whenContainsMixFunction_detectsErrors() {
        expect(
            // language=kotlin
            """
                package foo

                class SampleUseCase @Inject constructor(
                    private val sampleRepository: SampleRepository
                ) {

                    operator fun invoke(id: Int): Result<SampleData> {
                        val result = sampleRepository.getSampleData(id)
                        return when (result) {
                            is Result.Success -> Result.Success(result.data.toSampleData())
                            is Result.Error -> result
                        }.exhaustive
                    }

                    suspend operator fun invoke2(id: Int): Result<SampleData> {
                        val result = sampleRepository.getSampleData(id)
                        return when (result) {
                            is Result.Success -> Result.Success(result.data.toSampleData())
                            is Result.Error -> result
                        }.exhaustive
                    }
                }
            """.inKotlin
        ) toHave SomeError("src/foo/SampleUseCase.kt")
    }

    @Test
    fun whenContainsOnlySuspendFunction_detectsNoErrors() {
        expect(
            // language=kotlin
            """
                package foo

                class SampleUseCase @Inject constructor(
                    private val sampleRepository: SampleRepository
                ) {

                    suspend operator fun invoke(id: Int): Result<SampleData> {
                        val result = sampleRepository.getSampleData(id)
                        return when (result) {
                            is Result.Success -> Result.Success(result.data.toSampleData())
                            is Result.Error -> result
                        }.exhaustive
                    }
                }
            """.inKotlin
        ) toHave NoErrors
    }

    @Test
    fun whenNoUseCase_containsNotSuspendFunction_detectsNoErrors() {
        expect(
            // language=kotlin
            """
                package foo

                class SampleLogic @Inject constructor(
                    private val sampleRepository: SampleRepository
                ) {

                    operator fun invoke(id: Int): Result<SampleData> {
                        val result = sampleRepository.getSampleData(id)
                        return when (result) {
                            is Result.Success -> Result.Success(result.data.toSampleData())
                            is Result.Error -> result
                        }.exhaustive
                    }
                }
            """.inKotlin
        ) toHave NoErrors
    }

}