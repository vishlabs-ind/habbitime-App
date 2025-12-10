import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.plugin)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.rach.habitchange"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.rach.habitchange"
        minSdk = 24
        targetSdk = 35
        versionCode = 4
        versionName = "1.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //buildConfigField("String","ADMOB_APP_ID","\"${getSecretInfo("ADMOB_APP_ID")}\"")
        buildConfigField("String","BANNER_ADS_UNIT_ID","\"${getSecretInfo("BANNER_ADS_UNIT_ID")}\"")
        manifestPlaceholders["ADMOB_APP_ID"] = getSecretInfo("ADMOB_APP_ID")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.play.services.ads.api)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.coil.compose)

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.complier)
    implementation(libs.hilt.navagtionCompose)
    implementation(libs.androidx.paging.compose)

    implementation(libs.room.database)
    kapt(libs.room.complier)
    implementation(libs.room.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)

}

fun getSecretInfo(key:String):String{
    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()){
        properties.load(FileInputStream(localPropertiesFile))
        return properties[key]?.toString() ?: ""
    }

    return ""
}