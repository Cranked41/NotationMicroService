package com.microservice.notation.config

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;


import java.util.Locale;

@Configuration
class LocaleResolverConfig {
    @Bean
    fun localeResolver(): LocaleResolver {
        val acceptHeaderLocaleResolver = AcceptHeaderLocaleResolver()
        acceptHeaderLocaleResolver.setDefaultLocale(Locale.getDefault())
        return acceptHeaderLocaleResolver
    }
}