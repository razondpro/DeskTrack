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

        /**
         * basic email validation
         */
        fun isValidEmail(email: String?): Boolean {
            return !email.isNullOrBlank()
                    && email.contains(
                Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+\$")
            )
        }
    }
}