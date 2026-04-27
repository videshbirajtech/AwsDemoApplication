FROM eclipse-temurin:17-jdk-jammy

# Install ffmpeg and common codecs
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY target/*.jar awsdemo.jar
EXPOSE 9003
CMD ["java", "-jar", "awsdemo.jar"]
