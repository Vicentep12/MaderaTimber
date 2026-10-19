package com.duoc.madertimber.ui.screens.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthViewModelTest {
    @Test
    fun emptyLoginShowsRequiredErrorsWithoutRegistrationErrors() {
        val model = LoginViewModel()
        model.onEvent(AuthEvent.Submit)
        assertTrue(model.uiState.errors.email)
        assertTrue(model.uiState.errors.password)
        assertFalse(model.uiState.errors.name)
        assertFalse(model.uiState.errors.confirmation)
        assertFalse(model.uiState.showNotice)
    }

    @Test
    fun validLoginOnlyShowsDemoNoticeAndAllowsDismissal() {
        val model = LoginViewModel()
        model.onEvent(AuthEvent.EmailChanged(" estudiante@example.com "))
        model.onEvent(AuthEvent.PasswordChanged("abc"))
        model.onEvent(AuthEvent.Submit)
        assertFalse(model.uiState.errors.hasErrors)
        assertTrue(model.uiState.showNotice)
        model.onEvent(AuthEvent.DismissNotice)
        assertFalse(model.uiState.showNotice)
    }

    @Test
    fun registrationRejectsInvalidFieldsAndUpdatesErrorsAfterEditing() {
        val model = RegisterViewModel()
        model.onEvent(AuthEvent.NameChanged("   "))
        model.onEvent(AuthEvent.EmailChanged("incorrecto@"))
        model.onEvent(AuthEvent.PasswordChanged("corta"))
        model.onEvent(AuthEvent.ConfirmationChanged("otra"))
        model.onEvent(AuthEvent.Submit)
        assertTrue(model.uiState.errors.name)
        assertTrue(model.uiState.errors.email)
        assertTrue(model.uiState.errors.password)
        assertTrue(model.uiState.errors.confirmation)
        assertFalse(model.uiState.showNotice)
        model.onEvent(AuthEvent.NameChanged("Estudiante"))
        model.onEvent(AuthEvent.EmailChanged("estudiante@example.cl"))
        model.onEvent(AuthEvent.PasswordChanged("clave123"))
        model.onEvent(AuthEvent.ConfirmationChanged("clave123"))
        assertFalse(model.uiState.errors.hasErrors)
        assertFalse(model.uiState.showNotice)
        model.onEvent(AuthEvent.Submit)
        assertTrue(model.uiState.showNotice)
    }

    @Test
    fun whitespacePasswordIsRejectedEvenWithEightCharacters() {
        val model = RegisterViewModel()
        model.onEvent(AuthEvent.PasswordChanged("        "))
        model.onEvent(AuthEvent.ConfirmationChanged("        "))
        model.onEvent(AuthEvent.Submit)
        assertTrue(model.uiState.errors.password)
    }

    @Test
    fun leavingFormClearsCredentialsVisibilityAndErrors() {
        val model = RegisterViewModel()
        model.onEvent(AuthEvent.PasswordChanged("clave123"))
        model.onEvent(AuthEvent.ConfirmationChanged("clave123"))
        model.onEvent(AuthEvent.TogglePassword)
        model.onEvent(AuthEvent.Submit)
        model.reset()
        assertEquals(AuthUiState(), model.uiState)
    }

    @Test
    fun loginAndRegistrationDoNotShareState() {
        val login = LoginViewModel()
        val register = RegisterViewModel()
        login.onEvent(AuthEvent.EmailChanged("estudiante@example.com"))
        assertEquals("", register.uiState.email)
        assertFalse(login.uiState.errors.hasErrors)
        assertFalse(login.uiState.submitted)
    }
}
