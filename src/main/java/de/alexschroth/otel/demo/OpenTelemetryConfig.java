package de.alexschroth.otel.demo;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.ResourceAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfig {

	@Bean
	SdkLoggerProvider sdkLoggerProvider(@Value("${spring.application.name}") String serviceName) {
		return SdkLoggerProvider.builder()
			.addResource(
				Resource.create(
					Attributes.of(ResourceAttributes.SERVICE_NAME, serviceName)
				)
			)
			.addLogRecordProcessor(
				BatchLogRecordProcessor.builder(
						OtlpHttpLogRecordExporter.builder()
							.setEndpoint("http://localhost:4318/v1/logs")
							.build()
					)
					.build()
			)
			.build();
	}

	@Bean
	ApplicationListener<ApplicationReadyEvent> setupLogbackOtelAppender(OpenTelemetry openTelemetry) {
		return event -> OpenTelemetryAppender.install(openTelemetry);
	}
}
