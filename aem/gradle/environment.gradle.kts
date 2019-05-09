import com.cognifide.gradle.aem.common.AemExtension

configure<AemExtension> {
    environment {
        hosts(
                "127.0.0.1 example.com",
                "127.0.0.1 demo.example.com",
                "127.0.0.1 author.example.com",
                "127.0.0.1 invalidation-only"
        )
        directories(
                "logs",
                "cache/content/example/live",
                "cache/content/example/demo"
        )
        healthChecks {
            url("Live site", "http://example.com/en-us.html", text = "English")
            url("Demo site", "http://demo.example.com/en-us.html", text = "English")
            url("Author login", "http://author.example.com/libs/granite/core/content/login.html" +
                    "?resource=%2F&\$\$login\$\$=%24%24login%24%24&j_reason=unknown&j_reason_code=unknown", text = "AEM Sign In")
        }
    }

    tasks {
        instanceSetup {
            dependsOn(":develop")
        }

        instanceSatisfy {
            packages {
                group("dep.vanity-urls") { /* local("pkg/vanityurls-components-1.0.2.zip") */ }
                group("dep.kotlin") { dependency("org.jetbrains.kotlin:kotlin-osgi-bundle:${Build.KOTLIN_VERSION}") }
                group("dep.acs-aem-commons") { url("https://github.com/Adobe-Consulting-Services/acs-aem-commons/releases/download/acs-aem-commons-4.0.0/acs-aem-commons-content-4.0.0-min.zip") }
                group("tool.aem-easy-content-upgrade") { url("https://github.com/valtech/aem-easy-content-upgrade/releases/download/1.4.0/aecu.bundle-1.4.0.zip") }
                group("tool.search-webconsole-plugin") { dependency("com.neva.felix:search-webconsole-plugin:1.2.0") }
            }
        }

        // here is a desired place for defining custom AEM tasks

        register("environmentClean") {
            description = "Cleans AEM dispatcher cache"
            doLast {
                delete(fileTree(aem.environment.rootDir) { 
                    include("cache/**")
                })
            }
        }
    }
}
