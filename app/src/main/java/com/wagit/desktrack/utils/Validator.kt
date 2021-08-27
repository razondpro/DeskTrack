package com.wagit.desktrack.utils

import java.util.regex.Pattern

class Validator {
    companion object {
        // a simple validation
        //TODO implement a strong validation

        fun isValidCIF(cif: String?): Boolean {
            return !cif.isNullOrBlank()
                    && (cif.contains(Regex("^(\\d{8})([A-Za-z])\$"))
                    || cif.contains(Regex("^[A-Za-z]\\d{7,8}[A-Za-z]\$")))

            /*
            return !cif.isNullOrBlank()
                    && (cif.contains(Regex
                ("((([X-Z])|([LM])){1}([-]?)((\\d){7})([-]?)([A-Z]{1}))|((\\d{8})([-]?)([A-Z]))")))

             */
        }

        fun isValidNIF(nif: String?): Boolean {
            return !nif.isNullOrBlank() && (nif.contains(Regex("^(\\d{7})\$")))
        }

        fun isValidCCC(ccc: String?): Boolean {
            return !ccc.isNullOrBlank() && (ccc.contains(Regex("^(\\d{6})\$")))
        }
        /**
         * basic email validation
         */
        fun isValidEmail(email: String?): Boolean {
            /*
            return !email.isNullOrBlank()
                    && email.contains(
                Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+\$")
            )
             */
            val EMAIL_ADDRESS_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
            )

            return !email.isNullOrBlank() &&
                    EMAIL_ADDRESS_PATTERN.matcher(email).matches()
        }
    }
}