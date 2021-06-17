package com.wagit.desktrack.utils

class Validator {
    companion object {
        // a simple validation
        //TODO implement a strong validation
        fun isValidCIF(cif: String?): Boolean {
            return !cif.isNullOrBlank()
                    && (cif.contains(Regex("^(\\d{8})([A-Za-z])\$"))
                    || cif.contains(Regex("^[A-Za-z]\\d{7,8}[A-Za-z]\$")))
        }
    }
}