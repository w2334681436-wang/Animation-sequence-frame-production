plugins { id("com.android.application") }
android {
    namespace = "com.phainon.emberfall"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.phainon.emberfall"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }
    buildTypes { release { isMinifyEnabled = false } }
}
