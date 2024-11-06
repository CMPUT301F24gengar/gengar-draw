plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gengardraw"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gengardraw"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

configurations.all {
    exclude(group = "com.google.protobuf", module = "protobuf-lite")
}

dependencies {
    implementation(platform(libs.firebase.bom.v3350))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)


    androidTestImplementation(libs.espresso.contrib)
    androidTestImplementation("androidx.test:core:1.4.0")


    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")


    implementation(libs.glide)
    implementation(libs.zxing.android.embedded)


    implementation(libs.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)


    implementation("com.google.android.gms:play-services-location:21.3.0")

    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
}