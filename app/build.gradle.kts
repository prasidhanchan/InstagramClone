import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.google.services)
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.instagramclone.android"
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        applicationId = "com.instagramclone.android"
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(project.rootProject.file("gradle.properties").inputStream())

        resValue("string", "facebook_app_id", "\"${properties.getProperty("facebook_app_id")}\"")
        resValue("string", "facebook_login_protocol_scheme", "\"${properties.getProperty("facebook_login_protocol_scheme")}\"")
        resValue("string", "facebook_client_token", "\"${properties.getProperty("facebook_client_token")}\"")
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = AndroidConfig.kotlinCompilerExtensionVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // ExoPlayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // Coil
    implementation(libs.coil.compose)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Firebase BoM
    implementation(platform(libs.firebase.bom))

    // Firebase Firestore
    implementation (libs.firebase.firestore)

    // Firebase Authentication
    implementation(libs.firebase.authentication)

    // Firebase Storage
    implementation (libs.firebase.storage)

    // Facebook SDK
    implementation(libs.facebook.sdk)

    // Room DB
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.junit)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.compose.manifest)

    implementation(project(":core:ui"))
    implementation(project(":core:util"))
    implementation(project(":data:firebase"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:profile"))
}