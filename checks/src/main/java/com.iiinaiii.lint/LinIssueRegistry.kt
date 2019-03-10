package com.iiinaiii.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class LinIssueRegistry : IssueRegistry() {
    override val api: Int
        get() = CURRENT_API

    override val minApi: Int
        get() = -1

    override val issues: List<Issue> = listOf(
        CheckHttpDirectDetector.issue
    )
}