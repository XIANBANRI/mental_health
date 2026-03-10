package com.sl.mentalhealth.config;

import com.sl.mentalhealth.kafka.KafkaTopics;
import com.sl.mentalhealth.kafka.message.LoginRequestMessage;
import com.sl.mentalhealth.kafka.message.LoginResponseMessage;
import com.sl.mentalhealth.kafka.message.ResetPasswordRequestMessage;
import com.sl.mentalhealth.kafka.message.ResetPasswordResponseMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

  private static final String BOOTSTRAP_SERVERS = "localhost:9092";

  @Bean
  public NewTopic loginRequestTopic() {
    return new NewTopic(KafkaTopics.LOGIN_REQUEST, 1, (short) 1);
  }

  @Bean
  public NewTopic loginResponseTopic() {
    return new NewTopic(KafkaTopics.LOGIN_RESPONSE, 1, (short) 1);
  }

  @Bean
  public NewTopic resetPasswordRequestTopic() {
    return new NewTopic(KafkaTopics.RESET_PASSWORD_REQUEST, 1, (short) 1);
  }

  @Bean
  public NewTopic resetPasswordResponseTopic() {
    return new NewTopic(KafkaTopics.RESET_PASSWORD_RESPONSE, 1, (short) 1);
  }

  @Bean
  public ProducerFactory<String, LoginRequestMessage> loginRequestProducerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, LoginRequestMessage> loginRequestKafkaTemplate() {
    return new KafkaTemplate<>(loginRequestProducerFactory());
  }

  @Bean
  public ProducerFactory<String, LoginResponseMessage> loginResponseProducerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, LoginResponseMessage> loginResponseKafkaTemplate() {
    return new KafkaTemplate<>(loginResponseProducerFactory());
  }

  @Bean
  public ProducerFactory<String, ResetPasswordRequestMessage> resetPasswordRequestProducerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, ResetPasswordRequestMessage> resetPasswordRequestKafkaTemplate() {
    return new KafkaTemplate<>(resetPasswordRequestProducerFactory());
  }

  @Bean
  public ProducerFactory<String, ResetPasswordResponseMessage> resetPasswordResponseProducerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, ResetPasswordResponseMessage> resetPasswordResponseKafkaTemplate() {
    return new KafkaTemplate<>(resetPasswordResponseProducerFactory());
  }

  @Bean
  public ConsumerFactory<String, LoginRequestMessage> loginRequestConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "mh-login-request-group");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

    JacksonJsonDeserializer<LoginRequestMessage> deserializer =
        new JacksonJsonDeserializer<>(LoginRequestMessage.class);
    deserializer.addTrustedPackages("com.sl.mentalhealth.kafka.message");

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, LoginRequestMessage>
  loginRequestKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, LoginRequestMessage> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(loginRequestConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, LoginResponseMessage> loginResponseConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "mh-login-response-group");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

    JacksonJsonDeserializer<LoginResponseMessage> deserializer =
        new JacksonJsonDeserializer<>(LoginResponseMessage.class);
    deserializer.addTrustedPackages("com.sl.mentalhealth.kafka.message");

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, LoginResponseMessage>
  loginResponseKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, LoginResponseMessage> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(loginResponseConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, ResetPasswordRequestMessage> resetPasswordRequestConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "mh-reset-password-request-group");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

    JacksonJsonDeserializer<ResetPasswordRequestMessage> deserializer =
        new JacksonJsonDeserializer<>(ResetPasswordRequestMessage.class);
    deserializer.addTrustedPackages("com.sl.mentalhealth.kafka.message");

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ResetPasswordRequestMessage>
  resetPasswordRequestKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, ResetPasswordRequestMessage> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(resetPasswordRequestConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, ResetPasswordResponseMessage> resetPasswordResponseConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "mh-reset-password-response-group");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

    JacksonJsonDeserializer<ResetPasswordResponseMessage> deserializer =
        new JacksonJsonDeserializer<>(ResetPasswordResponseMessage.class);
    deserializer.addTrustedPackages("com.sl.mentalhealth.kafka.message");

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ResetPasswordResponseMessage>
  resetPasswordResponseKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, ResetPasswordResponseMessage> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(resetPasswordResponseConsumerFactory());
    return factory;
  }
}