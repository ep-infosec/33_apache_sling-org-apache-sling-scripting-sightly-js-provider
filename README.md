[![Apache Sling](https://sling.apache.org/res/logos/sling.png)](https://sling.apache.org)

&#32;[![Build Status](https://ci-builds.apache.org/job/Sling/job/modules/job/sling-org-apache-sling-scripting-sightly-js-provider/job/master/badge/icon)](https://ci-builds.apache.org/job/Sling/job/modules/job/sling-org-apache-sling-scripting-sightly-js-provider/job/master/)&#32;[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=apache_sling-org-apache-sling-scripting-sightly-js-provider&metric=coverage)](https://sonarcloud.io/dashboard?id=apache_sling-org-apache-sling-scripting-sightly-js-provider)&#32;[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=apache_sling-org-apache-sling-scripting-sightly-js-provider&metric=alert_status)](https://sonarcloud.io/dashboard?id=apache_sling-org-apache-sling-scripting-sightly-js-provider)&#32;[![JavaDoc](https://www.javadoc.io/badge/org.apache.sling/org.apache.sling.scripting.sightly.js.provider.svg)](https://www.javadoc.io/doc/org.apache.sling/org.apache.sling.scripting.sightly.js.provider)&#32;[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.apache.sling/org.apache.sling.scripting.sightly.js.provider/badge.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.apache.sling%22%20a%3A%22org.apache.sling.scripting.sightly.js.provider%22)&#32;[![scripting](https://sling.apache.org/badges/group-scripting.svg)](https://github.com/apache/sling-aggregator/blob/master/docs/groups/scripting.md) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# Apache Sling Scripting HTL JavaScript Use Provider

This module is part of the [Apache Sling](https://sling.apache.org) project.

This bundle allows HTL's Use API to access JS scripts. It also wraps Sling's JS engine in a simulated event loop.

The bundle also contains a bindings values provider that adds an API layer accessible from HTL & JS. The implementation of that API can be found in `src/main/resources/SLING-INF`.
