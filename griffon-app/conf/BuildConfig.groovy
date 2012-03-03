griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        griffonHome()
        
        mavenCentral()
        mavenRepo 'http://m2.terrastore.googlecode.com/hg/repo'
        mavenRepo 'http://repository.jboss.org/nexus/content/groups/public'
        mavenRepo 'http://download.java.net/maven/2/'
    }
    dependencies {
        compile('terrastore:terrastore-javaclient:2.4.1',
                'org.jboss.resteasy:jaxrs-api:2.0.0.GA',
                'org.jboss.resteasy:resteasy-jaxrs:2.0.0.GA',
                'commons-io:commons-io:2.0',
                'commons-lang:commons-lang:2.4',
                'org.codehaus.jackson:jackson-jaxrs:1.9.0') {
            exclude group: 'org.slf4j'
            excludes 'httpclient', 'junit'            
        }
        compile 'commons-httpclient:commons-httpclient:3.1'
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    appenders {
        console name: 'stdout', layout: pattern(conversionPattern: '%d [%t] %-5p %c - %m%n')
    }

    error 'org.codehaus.griffon',
          'org.springframework',
          'org.apache.karaf',
          'groovyx.net'
    warn  'griffon'
}