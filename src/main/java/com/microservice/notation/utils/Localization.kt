package com.microservice.notation.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class Localization {

    @Autowired
    private lateinit var messageSource: MessageSource

    fun getMessage(translationKey: String): String {
        return messageSource.getMessage(translationKey, null, LocaleContextHolder.getLocale())
    }
}