FROM apache/airflow:2.5.1

USER root

# Fix permission issue with apt
RUN rm -rf /var/lib/apt/lists/partial && \
    apt-get clean && \
    apt-get update

# Install OpenJDK (Java)
RUN apt-get install -y openjdk-11-jdk

# Set the JAVA_HOME environment variable
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64

# Add your existing commands below
ADD requirements.txt .


# Set permissions for the Java executable
RUN chmod +x /usr/lib/jvm/java-11-openjdk-amd64/bin/java

# Switch back to the airflow user
USER airflow

