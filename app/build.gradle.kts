import java.util.regex.Pattern

plugins {
    alias(libs.plugins.hilt)

    id("com.android.application")
    kotlin("android")
    kotlin("plugin.parcelize")
    kotlin("kapt")

//    id("com.google.gms.google-services")
//    id("com.google.firebase.appdistribution")
//    id("com.google.firebase.crashlytics")
//    id("com.google.firebase.firebase-perf")

    alias(libs.plugins.grgit)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "org.mpdx.example"

    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }

    defaultConfig {
        applicationId = "org.mpdx.example"
        versionName = project.version.toString()
        versionCode = grgit.log(mapOf("includes" to listOf("HEAD"))).size

        manifestPlaceholders += mapOf("appAuthRedirectScheme" to "org.mpdx")

        buildConfigField("String", "MPDX_API_BASE_URI", "\"https://api.mpdx.org/api/v2/\"")

        // region oauth BuildConfigs
        buildConfigField("String", "AUTH_END_POINT", "\"org.mpdx\"")
        buildConfigField("String", "TOKEN_END_POINT", "\"org.mpdx\"")
        buildConfigField("String", "CLIENT_ID", "\"org.mpdx\"")
        buildConfigField("String", "REDIRECT_URI", "\"org.mpdx\"")
        buildConfigField("String", "AUTH_PROVIDER", "\"org.mpdx\"")
        // endregion

        //region okta BuildConfigs
        buildConfigField("String", "OKTA_AUTH_SCHEME", "\"org.mpdx\"")
        buildConfigField("String", "OKTA_CLIENT_ID", "\"ajdklfajlkfjafda\"")
        buildConfigField("String", "OKTA_DISCOVERY_URI", "\"https://signon.okta.com\"")
        // endregion

        manifestPlaceholders += mapOf("hostMpdxWeb" to "mpdx.org")

        proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))

        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kapt {
        javacOptions {
            // Increase the max count of errors from annotation processors.
            // Default is 100.
            option("-Xmaxerrs", 9999)
        }
    }

    flavorDimensions.add("env")
    productFlavors {
        register("production") {
            dimension = "env"
        }
        register("development") {
            dimension = "env"
        }
    }
}

dependencies {
    implementation(libs.mpdx.auth.oauth)
//    implementation(libs.mpdx.auth.okta)
    implementation(libs.mpdx.core)
    implementation(libs.mpdx.library)

    implementation(libs.androidx.core.ktx)
    implementation(libs.openid.appauth)

    implementation(libs.firebase.core)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.iid)

    implementation(libs.gtoSupport.api.okhttp3)
    implementation(libs.gtoSupport.base)
    implementation(libs.gtoSupport.compat)
    implementation(libs.gtoSupport.core)
    implementation(libs.gtoSupport.dagger)
    implementation(libs.gtoSupport.firebase.crashlytics)
    implementation(libs.gtoSupport.facebook.flipper)
    implementation(libs.gtoSupport.jsonapi.retrofit2)
    implementation(libs.gtoSupport.kotlin.coroutines)
    implementation(libs.gtoSupport.okhttp3)
    implementation(libs.gtoSupport.util)
    implementation(libs.gtoSupport.okta)

    implementation(libs.weakDelegate)
    implementation(libs.eventbus)
    implementation(libs.gson)
    implementation(libs.guava)
    implementation(libs.hilt)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.coroutines.playServices)
    implementation(libs.materialComponents)
    implementation(libs.threetenbp.android)
    implementation(libs.timber)

    // debugging tools
    debugImplementation(libs.facebook.flipper)
    debugImplementation(libs.facebook.flipper.plugins.network)
    debugImplementation(libs.facebook.flipper.plugins.realm)
    debugImplementation(libs.facebook.soloader)
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.okhttp3.interceptors.logging)
    debugImplementation(libs.gtoSupport.leakcanary)

    kapt(libs.dagger.compiler)
    kapt(libs.hilt.compiler)
}

fun getCurrentFlavor(): String {
    val taskRequestsStr = gradle.startParameter.taskRequests.toString()
    val pattern: Pattern = if (taskRequestsStr.contains("assemble")) {
        Pattern.compile("assemble(\\w+)(Release|Debug)")
    } else {
        Pattern.compile("bundle(\\w+)(Release|Debug)")
    }

    val matcher = pattern.matcher(taskRequestsStr)
    val flavor = if (matcher.find()) {
        matcher.group(1).lowercase()
    } else {
        print("NO FLAVOR FOUND")
        ""
    }
    return flavor
}
