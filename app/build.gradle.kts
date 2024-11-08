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
    resolutionStrategy.eachDependency {
        if (requested.group == "androidx.test" && requested.name == "core") {
            useVersion("1.5.0")
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom.v3350))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

    debugImplementation("androidx.fragment:fragment-testing:1.5.5")


    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")


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