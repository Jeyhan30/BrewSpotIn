plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.brewspotin"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.brewspotin"
        minSdk = 24
        targetSdk = 34
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
        kotlinCompilerExtensionVersion = "1.5.1" // Sesuaikan dengan versi Kotlin Compose plugin
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.coil.compose)

    implementation(platform("com.google.firebase:firebase-bom:33.14.0")) // <-- Periksa versi terbaru!
    implementation("com.google.firebase:firebase-auth-ktx") // Versi dikelola BoM
    implementation("com.google.firebase:firebase-database-ktx") // Versi dikelola BoM
    implementation("com.google.firebase:firebase-firestore-ktx") // Versi dikelola BoM
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.navigation.runtime.android)

    implementation(libs.kotlinx.coroutines.play.services) // For .await() Firebase Tasks

    // Core Coroutines
    implementation(libs.kotlinx.coroutines.core) // Pastikan ini ada jika ada error
    implementation(libs.androidx.lifecycle.viewmodel.compose)

}