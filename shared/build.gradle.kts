plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

version = "1.0"

kotlin {
    android()
    val iosTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {}

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = Versions.iOSDeploymentTarget
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
                //NETWORK
                implementation("io.ktor:ktor-client-core:${Versions.ktor}")
                implementation("io.ktor:ktor-client-json:${Versions.ktor}")
                implementation("io.ktor:ktor-client-logging:${Versions.ktor}")
                implementation("io.ktor:ktor-client-serialization:${Versions.ktor}")
                implementation("io.ktor:ktor-client-websockets:${Versions.ktor}")
                //DI
                implementation("io.insert-koin:koin-core:${Versions.koin}")
                //DATABASE
                implementation("com.squareup.sqldelight:runtime:${Versions.sql_delight}")
                //SERIALIZATION
                implementation("com.russhwolf:multiplatform-settings:${Versions.russhwolf}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:${Versions.ktor}")
                implementation("io.ktor:ktor-network-tls:${Versions.ktor}")
                implementation("com.squareup.okhttp3:okhttp:4.9.3")
                implementation("androidx.core:core:1.7.0")
                implementation("androidx.compose.ui:ui:${Versions.compose}")
                implementation("com.squareup.sqldelight:android-driver:${Versions.sql_delight}")
            }
        }
        val androidTest by getting

        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:${Versions.ktor}")
                implementation("com.squareup.sqldelight:native-driver:${Versions.sql_delight}")
            }

            val iosTest by getting
        }
    }
}

android {
    compileSdk = Versions.compile_sdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.min_sdk
        targetSdk = Versions.compile_sdk
    }
}

sqldelight {
    database("SqlDatabase") {
        packageName = "com.turbosokol.mypurchases"
    }
}
