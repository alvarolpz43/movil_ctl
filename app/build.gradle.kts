plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.movil_ctl"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.movil_ctl"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    val room_version = "2.6.1"


    // MongoDB Realm SDK
//    implementation("org.mongodb:mongodb-driver-sync:4.11.1")
//    implementation("io.realm.kotlin:library-sync:1.11.0")


    // WorkManager para sincronización periódica
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Room
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // 🔹 Hilt para inyección de dependencias
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    // 🔹 Compatibilidad con SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // 🔹 Jetpack Compose (VERSIONES ESPECÍFICAS)
    implementation("androidx.compose.ui:ui:1.6.2") // Última versión estable
    implementation("androidx.compose.ui:ui-graphics:1.6.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.2")

    // 🔹 Material 3 (Asegurar compatibilidad con Compose)
    implementation("androidx.compose.material3:material3:1.2.1")

    // 🔹 Material Icons Extendidos
    implementation("androidx.compose.material:material-icons-extended:1.6.2")

    // 🔹 Animaciones para evitar conflictos con `KeyframesSpec`
    implementation("androidx.compose.animation:animation:1.6.2")

    // 🔹 Navegación en Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // 🔹 Lifecycle para Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // 🔹 Retrofit para llamadas a API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // 🔹 DataStore para almacenamiento local
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // 🔹 Accompanist para SwipeRefresh
    implementation("com.google.accompanist:accompanist-swiperefresh:0.34.0")

    implementation("androidx.compose.runtime:runtime-livedata:1.6.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}