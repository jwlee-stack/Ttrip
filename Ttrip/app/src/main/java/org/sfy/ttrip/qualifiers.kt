package org.sfy.ttrip

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuthInterceptorClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshInterceptorClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FlaskInterceptorClient