package com.geekbrains.myfirsttests

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EmailValidatorTest {

    @Test
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(EmailValidator.isValidEmail("name@email.com"))
    }

    @Test
    fun emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(EmailValidator.isValidEmail("name@email.co.uk"))
    }

    @Test
    fun emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name@email"))
    }

    @Test
    fun emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name@email..com"))
    }

    @Test
    fun emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("@email.com"))
    }

    @Test
    fun emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail(""))
    }

    @Test
    fun emailValidator_NullEmail_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail(null))
    }

                /*_____________________________________________*/
    @Test
    fun emailValidator_InvalidEmailNoDog_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("nameemail.com"))
    }

    @Test
    fun emailValidator_InvalidEmailNoRightDog_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name&email.com"))
    }

    @Test
    fun emailValidator_InvalidEmailDowbleDog_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name@@email.com"))
    }

    @Test
    fun emailValidator_InvalidSimbols_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("!ddf%@inbox.com"))
    }

    @Test
    fun emailValidator_InvalidEmailNoTLast_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name@email."))
    }
}
