package org.sfy.ttrip.common.util

interface DeclarationDialogListener {
    fun postDeclaration(reportContext: String, reportedNickname: String)
}