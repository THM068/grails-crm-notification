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
        grailsHome()
        grailsCentral()
        mavenRepo "http://labs.technipelago.se/repo/crm-releases-local/"
        //mavenRepo "http://labs.technipelago.se/repo/plugin-releases-local/"
    }
    dependencies {
        //test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
    }

    plugins {
        build(":tomcat:$grailsVersion",
                ":release:2.2.1") {
            export = false
        }
        runtime(":hibernate:$grailsVersion") {
            export = false
        }

        test(":spock:0.7") {
            export = false
            //exclude "spock-grails-support"
        }
        test(":codenarc:0.18.1") { export = false }
        test(":code-coverage:1.2.6") { export = false }

        compile(":platform-core:1.0.RC5") { excludes 'resources' }

        compile "grails.crm:crm-core:latest.integration"
    }
}

codenarc {
    reports = {
        CrmXmlReport('xml') {
            outputFile = 'target/test-reports/CodeNarcReport.xml'
            title = 'Grails CRM CodeNarc Report'
        }
        CrmHtmlReport('html') {
            outputFile = 'target/test-reports/CodeNarcReport.html'
            title = 'Grails CRM CodeNarc Report'

        }
    }
    properties = {
        GrailsPublicControllerMethod.enabled = false
        CatchException.enabled = false
        CatchThrowable.enabled = false
        ThrowException.enabled = false
        ThrowRuntimeException.enabled = false
        GrailsStatelessService.enabled = false
        GrailsStatelessService.ignoreFieldNames="dataSource,scope,sessionFactory,transactional,*Service,messageSource,grailsApplication,applicationContext,expose"
    }
    processTestUnit = false
    processTestIntegration = false
}

coverage {
    exclusions = ['**/radar/**']
}

