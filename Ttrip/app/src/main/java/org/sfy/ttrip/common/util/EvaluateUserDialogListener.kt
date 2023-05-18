package org.sfy.ttrip.common.util

interface EvaluateUserDialogListener {
    fun evaluate(matchHistoryId: String, rate: Int)
    fun openDeclaration(reportedNickname: String)
}