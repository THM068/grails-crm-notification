grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.repos.default = "crm"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
    }
    log "warn"
    repositories {
        grailsCentral()
        mavenRepo "http://labs.technipelago.se/repo/crm-releases-local/"
        mavenRepo "http://labs.technipelago.se/repo/plugin-releases-local/"
    }
    dependencies {
    }

    plugins {
        build(":tomcat:$grailsVersion",
              ":hibernate:$grailsVersion",
              ":release:2.0.4",
              ":rest-client-builder:1.0.2") {
            export = false
        }

        test(":spock:0.6") { export = false }

        compile(":platform-core:1.0.M6") { excludes 'resources' }

        compile "grails.crm:crm-core:latest.integration"
    }
}
