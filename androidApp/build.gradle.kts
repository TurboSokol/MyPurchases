plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}

android {
    compileSdk = Versions.compile_sdk
    defaultConfig {
        applicationId = "com.turbosokol.mypurchases.android"
        minSdk = Versions.min_sdk
        targetSdk = Versions.compile_sdk
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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
        kotlinCompilerExtensionVersion = Versions.compose
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.6.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.core:core-ktx:1.7.0")

    //Compose
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
    implementation("androidx.compose.ui:ui-util:${Versions.compose}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}")
    implementation("androidx.constraintlayout:constraintlayout-compose:${Versions.constraint_layout_compose}")

    //ACCOMPANIST
    implementation("com.google.accompanist:accompanist-pager:0.20.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.20.0")
    implementation("com.google.accompanist:accompanist-insets:${Versions.accompanist}")
    implementation("com.google.accompanist:accompanist-insets-ui:${Versions.accompanist}")
    implementation("com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}")
    implementation("com.google.accompanist:accompanist-permissions:${Versions.accompanist}")
    implementation("com.google.accompanist:accompanist-swiperefresh:${Versions.accompanist}")

    //IMAGES
    implementation("io.coil-kt:coil-compose:2.0.0")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.4.2")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.21.4-beta")

    //KTOR
    implementation("io.ktor:ktor-client-android:${Versions.ktor}")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")

    // DI
    implementation("io.insert-koin:koin-core:${Versions.koin}")
    implementation("io.insert-koin:koin-android:${Versions.koin}")
    implementation("io.insert-koin:koin-androidx-compose:${Versions.koin}")

    //Tests
    implementation ("androidx.test:rules:${Versions.test}")
    implementation ("androidx.test:runner:${Versions.test}")
    implementation ("androidx.compose.ui:ui-test:${Versions.compose}")
    implementation ("androidx.compose.ui:ui-test-junit4:${Versions.compose}")
    implementation ("androidx.compose.ui:ui-test-junit4:${Versions.compose}")
    androidTestImplementation("androidx.test:core:${Versions.test}")
    androidTestImplementation("androidx.test:runner:${Versions.test}")
    androidTestImplementation("androidx.test:rules:${Versions.test}")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.compose}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.compose}")
    androidTestImplementation ("androidx.test:runner:${Versions.androidXTestVersion}")
    androidTestImplementation ("androidx.test:rules:${Versions.androidXTestVersion}")
    androidTestImplementation("io.mockk:mockk-android:${Versions.mock}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.compose}")

}