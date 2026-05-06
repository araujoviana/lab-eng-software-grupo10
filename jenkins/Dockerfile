FROM jenkins/jenkins:lts-jdk21

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
ENV ALLURE_VERSION=2.40.0

USER root
RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates curl git \
    && rm -rf /var/lib/apt/lists/*
RUN curl -fsSL "https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/${ALLURE_VERSION}/allure-commandline-${ALLURE_VERSION}.tgz" \
    | tar -xz -C /opt \
    && chown -R jenkins:jenkins "/opt/allure-${ALLURE_VERSION}"
USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
