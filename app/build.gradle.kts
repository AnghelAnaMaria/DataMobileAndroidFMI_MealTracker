
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "cst.unibucfmi2026"
    compileSdk =36


    defaultConfig {
        applicationId = "cst.unibucfmi2026"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig=true
    }

    flavorDimensions +="environment"
    productFlavors{
        create("dev"){
            dimension="environment"
            applicationIdSuffix=".dev"
            versionCode=1
            versionName="1.0.0-dev"
            buildConfigField("String","BASE_URL", " \"https://dev://dev.myapp.com\" ")
        }
        create("qa"){
            dimension="environment"
            applicationIdSuffix=".qa"
            versionCode=1
            versionName="1.0.6-qa"
            buildConfigField("String","BASE_URL", "\"https://dev://qa.myapp.com\"")
        }
        create("prod"){
            dimension="environment"
            buildConfigField("String","BASE_URL", "\"https://dev://myapp.com\"")
        }
    }
}


dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
   // implementation(platform(libs.firebase.bom))
   // implementation(libs.firebase.analytics)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.espresso.core)
    implementation(libs.material)
    implementation(libs.androidx.compose.material.icons.extended)
    testImplementation(libs.junit)
   // implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}