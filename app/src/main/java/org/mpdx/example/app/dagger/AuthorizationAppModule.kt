package org.mpdx.example.app.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.ccci.gto.android.common.dagger.eager.EagerModule
import org.mpdx.android.library.oauth.listener.OAuthAppListener
import org.mpdx.example.BuildConfig

// import org.mpdx.android.library.okta.listener.OktaAppListener

@InstallIn(SingletonComponent::class)
@Module(includes = [EagerModule::class])
class AuthorizationAppModule {

//    @Provides
//    fun providesOktaAppConstant(): OktaAppListener {
//        return object : OktaAppListener {
//            override fun oktaDiscoveryUri() = BuildConfig.OKTA_DISCOVERY_URI
//
//            override fun oktaClientId() = BuildConfig.OKTA_CLIENT_ID
//
//            override fun oktaAuthScheme() = BuildConfig.OKTA_AUTH_SCHEME
//
//            override fun tabColor() = R.color.primary_blue
//        }
//    }

    @Provides
    fun providesOAuthAppConstant(): OAuthAppListener {
        return object : OAuthAppListener {
            override fun authProvider() = BuildConfig.AUTH_PROVIDER

            override fun authorizationEndPoint() = BuildConfig.AUTH_END_POINT

            override fun clientId() = BuildConfig.CLIENT_ID

            override fun redirectUri() = BuildConfig.REDIRECT_URI

            override fun tokenEndPoint() = BuildConfig.TOKEN_END_POINT
        }
    }
}
