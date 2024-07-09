plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.rasabot"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.rasabot"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packagingOptions {
        // Merge all files with the same path
        merge ("META-INF/DEPENDENCIES")
        merge ("META-INF/INDEX.LIST")
        merge ("versionchanges.txt")
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("androidx.appcompat:appcompat:1.3.0")
    implementation ("com.google.android.material:material:1.4.0")
//    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.itextpdf:itext7-core:7.1.15")
//    implementation ("net.sourceforge.tess4j:tess4j:4.5.4")
//    implementation ("javax.media:jai_core:1.1.3")
//    implementation ("com.google.firebase:firebase-ml-vision-face-model:20.0.0")





}