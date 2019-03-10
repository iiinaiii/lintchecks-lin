package com.iiinaiii.lint

import com.android.tools.lint.detector.api.Scope
import com.serchinastico.lin.annotations.Detector
import com.serchinastico.lin.dsl.detector
import com.serchinastico.lin.dsl.issue
import org.jetbrains.uast.getValueIfStringLiteral

@Detector
fun checkHttpDirect() = detector(
    issue(
        scope = Scope.JAVA_FILE_SCOPE,
        description = "Don't Write http url direct in code",
        explanation = """
            | API endpoint should be managed by one class.
            | To avoid ship application with development environment endpoint,
            | it shouldn't write http/https url direct in code.
            | """.trimMargin()
    )
) {
    literalExpression {
        suchThat { node ->
            val stringLiteral = node.getValueIfStringLiteral() ?: return@suchThat false
            return@suchThat stringLiteral.contains("http://") || stringLiteral.contains("https://")
        }
    }
}