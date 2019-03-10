package com.iiinaiii.lint

import com.android.tools.lint.detector.api.Scope
import com.serchinastico.lin.annotations.Detector
import com.serchinastico.lin.dsl.detector
import com.serchinastico.lin.dsl.issue
import org.jetbrains.uast.UClass

@Detector
fun checkUseCaseSuspendFunction() = detector(
    issue(
        scope = Scope.JAVA_FILE_SCOPE,
        description = "UseCase function must be suspend function.",
        explanation = """This project adopts a layered architecture,
            and the function of the UseCase layer must be a suspend function
        """.trimIndent()
    )
) {
    type {
        suchThat { it.onlyHasSuspendFunction }
    }
}

private inline val UClass.onlyHasSuspendFunction: Boolean
    get() {
        if (name?.contains("UseCase") == false) {
            return false
        }

        return methods.filter {
            it.isConstructor.not()
        }.all {
            it.modifierList.text.contains("suspend")
        }.not()

    }