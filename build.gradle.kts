import org.gradle.api.tasks.testing.logging.TestExceptionFormat

allprojects {
    group = "com.strumenta.kuki"
	 repositories {
		mavenLocal()
		mavenCentral()
	 }
}

tasks.wrapper {
    gradleVersion = "8.6"
    distributionType = Wrapper.DistributionType.ALL
}

subprojects {
    tasks.withType(Test::class).all {
        testLogging {
            showStandardStreams = true
            showExceptions = true
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}