package com.nice.nice.report.models

data class Report(val name: String, val desc: String, val file: String)  {
    companion object {
        const val COLLECTION_KEY = "Report"
        const val NAME_KEY = "Name"
        const val DESC_KEY = "Desc"
        const val FILE_KEY = "FileUrl"
    }
}