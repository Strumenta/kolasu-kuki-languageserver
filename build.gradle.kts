allprojects {
    group = "com.strumenta.kuki"
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
            exceptionFormat =
                org
                    .gradle
                    .api
                    .tasks
                    .testing
                    .logging
                    .TestExceptionFormat
                    .FULL
        }
    }
}