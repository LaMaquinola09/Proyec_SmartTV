plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt") // Ya está presente en tu archivo
}

android {
    namespace = "com.example.myappsmart_tv"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myappsmart_tv"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Compose dependencies
    implementation ("androidx.compose.ui:ui:1.5.1")
    implementation ("androidx.compose.material:material:1.5.1")
    implementation ("androidx.navigation:navigation-compose:2.5.3")

    // Retrofit for networking
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    // Coil for image loading
    implementation ("io.coil-kt:coil-compose:2.4.0")


    implementation ("androidx.compose.foundation:foundation:1.4.3")
    implementation ("androidx.compose.material:material:1.4.3")

    // Glide Compose integration
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")
    // Glide core library
    implementation("com.github.bumptech.glide:glide:4.13.0")
    // Kapt dependency for Glide annotation processing
    kapt("com.github.bumptech.glide:compiler:4.13.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")






    // Librerías basadas en el catálogo de versiones (libs.versions.toml)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Test dependencies
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
