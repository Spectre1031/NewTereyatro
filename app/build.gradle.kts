plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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
        kotlinCompilerExtensionVersion = "1.4.4"
    }
}

val roomVersion       = "2.7.1"
val lifecycleVersion  = "2.6.1"
val coroutinesVersion = "1.7.0"
val hiltVersion = "2.51.1"
val navVersion        = "2.7.7"
val datastoreVersion  = "1.0.0"

dependencies {
    // --- Room ---
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation(libs.androidx.runtime.livedata)
    kapt          ("androidx.room:room-compiler:$roomVersion")

    // --- Hilt ---
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt          ("com.google.dagger:hilt-compiler:$hiltVersion")

    implementation("androidx.compose.material:material-icons-extended:1.4.0")

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // --- Lifecycle & LiveData ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    // --- Coroutines ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // --- Navigation Compose ---
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // --- Compose UI ---
    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.ui:ui-graphics:1.4.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0")
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    // --- Coil (image loading) ---
    implementation("io.coil-kt:coil-compose:2.4.0")

    // --- DataStore (if still used) ---
    implementation("androidx.datastore:datastore-preferences:$datastoreVersion")

    // --- Testing ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.0")
}
