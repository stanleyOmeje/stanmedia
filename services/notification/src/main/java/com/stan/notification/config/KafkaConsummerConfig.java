//package com.stan.notification.config;
//
//import com.stan.notification.kafka.order.OrderConfirmation;
//import com.stan.notification.kafka.payment.PaymentConfirmation;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaConsummerConfig {
//
//        @Value("${spring.kafka.bootstrap-servers}")
//        private String bootstrapServers;
//
//        private Map<String, Object> baseConsumerProps() {
//            Map<String, Object> props = new HashMap<>();
//            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//            props.put(ConsumerConfig.GROUP_ID_CONFIG, "notificationGroup");
//            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//            props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
//            props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
//            props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//            return props;
//        }
//
//        @Bean
//        public ConsumerFactory<String, OrderConfirmation> orderConsumerFactory() {
//            Map<String, Object> props = baseConsumerProps();
//            props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, OrderConfirmation.class.getName());
//            props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
//            return new DefaultKafkaConsumerFactory<>(props);
//        }
//
//        @Bean
//        public ConcurrentKafkaListenerContainerFactory<String, OrderConfirmation> orderKafkaListenerContainerFactory() {
//            ConcurrentKafkaListenerContainerFactory<String, OrderConfirmation> factory = new ConcurrentKafkaListenerContainerFactory<>();
//            factory.setConsumerFactory(orderConsumerFactory());
//            return factory;
//        }
//
//        @Bean
//        public ConsumerFactory<String, PaymentConfirmation> paymentConsumerFactory() {
//            Map<String, Object> props = baseConsumerProps();
//            props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, PaymentConfirmation.class.getName());
//            props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
//            return new DefaultKafkaConsumerFactory<>(props);
//        }
//
//        @Bean
//        public ConcurrentKafkaListenerContainerFactory<String, PaymentConfirmation> paymentKafkaListenerContainerFactory() {
//            ConcurrentKafkaListenerContainerFactory<String, PaymentConfirmation> factory = new ConcurrentKafkaListenerContainerFactory<>();
//            factory.setConsumerFactory(paymentConsumerFactory());
//            return factory;
//        }
//
//
//}


package com.stan.notification.config;

import com.stan.notification.kafka.order.OrderConfirmation;
import com.stan.notification.kafka.payment.PaymentConfirmation;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer; // ✅ replaces JsonDeserializer

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsummerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseConsumerProps() {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notificationGroup");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class); // ✅
        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "*");                                     // ✅
        return props;
    }


    private <T> ConsumerFactory<String, T> buildConsumerFactory(Class<T> targetType) {
        var props = baseConsumerProps();
        props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, targetType.getName());   // ✅
        props.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false);               // ✅
        return new DefaultKafkaConsumerFactory<>(props);
    }


    private <T> ConcurrentKafkaListenerContainerFactory<String, T> buildListenerFactory(
        ConsumerFactory<String, T> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, T>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    // ── Order ────────────────────────────────────────────────────────────────

    @Bean
    public ConsumerFactory<String, OrderConfirmation> orderConsumerFactory() {
        return buildConsumerFactory(OrderConfirmation.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderConfirmation>
    orderKafkaListenerContainerFactory() {
        return buildListenerFactory(orderConsumerFactory());
    }

    // ── Payment ──────────────────────────────────────────────────────────────

    @Bean
    public ConsumerFactory<String, PaymentConfirmation> paymentConsumerFactory() {
        return buildConsumerFactory(PaymentConfirmation.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentConfirmation>
    paymentKafkaListenerContainerFactory() {
        return buildListenerFactory(paymentConsumerFactory());
    }
}
