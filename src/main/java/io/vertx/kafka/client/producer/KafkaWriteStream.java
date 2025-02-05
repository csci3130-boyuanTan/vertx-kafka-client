/*
 * Copyright 2016 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vertx.kafka.client.producer;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.WriteStream;
import io.vertx.kafka.client.common.KafkaClientOptions;
import io.vertx.kafka.client.producer.impl.KafkaWriteStreamImpl;
import io.vertx.kafka.client.serialization.VertxSerdes;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * A {@link WriteStream} for writing to Kafka {@link ProducerRecord}.
 * <p>
 * The {@link #write(Object)} provides global control over writing a record.
 * <p>
 */
public interface KafkaWriteStream<K, V> extends WriteStream<ProducerRecord<K, V>> {

  int DEFAULT_MAX_SIZE = 1024 * 1024;

  /**
   * Create a new KafkaWriteStream instance
   *
   * @param vertx Vert.x instance to use
   * @param config  Kafka producer configuration
   * @return  an instance of the KafkaWriteStream
   */
  static <K, V> KafkaWriteStream<K, V> create(Vertx vertx, Properties config) {
    return new KafkaWriteStreamImpl<>(
      vertx,
      new org.apache.kafka.clients.producer.KafkaProducer<>(config),
      KafkaClientOptions.fromProperties(config, true));
  }

  /**
   * Create a new KafkaWriteStream instance
   *
   * @param vertx Vert.x instance to use
   * @param config  Kafka producer configuration
   * @param keyType class type for the key serialization
   * @param valueType class type for the value serialization
   * @return  an instance of the KafkaWriteStream
   */
  static <K, V> KafkaWriteStream<K, V> create(Vertx vertx, Properties config, Class<K> keyType, Class<V> valueType) {
    Serializer<K> keySerializer = VertxSerdes.serdeFrom(keyType).serializer();
    Serializer<V> valueSerializer = VertxSerdes.serdeFrom(valueType).serializer();
    return create(vertx, config, keySerializer, valueSerializer);
  }

  /**
   * Create a new KafkaWriteStream instance
   *
   * @param vertx Vert.x instance to use
   * @param config  Kafka producer configuration
   * @param keySerializer key serializer
   * @param valueSerializer value serializer
   * @return  an instance of the KafkaWriteStream
   */
  static <K, V> KafkaWriteStream<K, V> create(Vertx vertx, Properties config, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
    return new KafkaWriteStreamImpl<>(
      vertx,
      new org.apache.kafka.clients.producer.KafkaProducer<>(config, keySerializer, valueSerializer),
      KafkaClientOptions.fromProperties(config, true));
  }

  /**
   * Create a new KafkaWriteStream instance
   *
   * @param vertx Vert.x instance to use
   * @param config  Kafka producer configuration
   * @return  an instance of the KafkaWriteStream
   */
  static <K, V> KafkaWriteStream<K, V> create(Vertx vertx, Map<String, Object> config) {
    return new KafkaWriteStreamImpl<>(
      vertx,
      new org.apache.kafka.clients.producer.KafkaProducer<>(config),
      KafkaClientOptions.fromMap(config, true));
  }

  /**
   * Create a new KafkaWriteStream instance
   *
   * @param vertx Vert.x instance to use
   * @param config  Kafka producer configuration
   * @param keyType class type for the key serialization
   * @param valueType class type for the value serialization
   * @return  an instance of the KafkaWriteStream
   */
  static <K, V> KafkaWriteStream<K, V> create(Vertx vertx, Map<String, Object> config, Class<K> keyType, Class<V> valueType) {
    Serializer<K> keySerializer = VertxSerdes.serdeFrom(keyType).serializer();
    Serializer<V> valueSerializer = VertxSerdes.serdeFrom(valueType).serializer();
    return create(vertx, config, keySerializer, valueSerializer);
  }

  /**
   * Create a new KafkaWriteStream instance
   *
   * @param vertx Vert.x instance to use
   * @param config  Kafka producer configuration
   * @param keySerializer key serializer
   * @param valueSerializer value serializer
   * @return  an instance of the KafkaWriteStream
   */
  static <K, V> KafkaWriteStream<K, V> create(Vertx vertx, Map<String, Object> config, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
    return new KafkaWriteStreamImpl<>(
      vertx,
      new org.apache.kafka.clients.producer.KafkaProducer<>(config, keySerializer, valueSerializer),
      KafkaClientOptions.fromMap(config, true));
  }

  /**
   * Create a new KafkaWriteStream instance
   *
   * @param vertx Vert.x instance to use
   * @param options  Kafka producer options
   * @return  an instance of the KafkaWriteStream
   */
  static <K, V> KafkaWriteStream<K, V> create(Vertx vertx, KafkaClientOptions options) {
    Map<String, Object> config = new HashMap<>();
    if (options.getConfig() != null) {
      config.putAll(options.getConfig());
    }
    return new KafkaWriteStreamImpl<>(vertx, new org.apache.kafka.clients.producer.KafkaProducer<>(config), options);
  }

  /**
   * Create a new KafkaWriteStream instance
   *
   * @param vertx Vert.x instance to use
   * @param options  Kafka producer options
   * @param keyType class type for the key serialization
   * @param valueType class type for the value serialization
   * @return  an instance of the KafkaWriteStream
   */
  static <K, V> KafkaWriteStream<K, V> create(Vertx vertx, KafkaClientOptions options, Class<K> keyType, Class<V> valueType) {
    Serializer<K> keySerializer = VertxSerdes.serdeFrom(keyType).serializer();
    Serializer<V> valueSerializer = VertxSerdes.serdeFrom(valueType).serializer();
    return create(vertx, options, keySerializer, valueSerializer);
  }

  /**
   * Create a new KafkaWriteStream instance
   *
   * @param vertx Vert.x instance to use
   * @param options  Kafka producer options
   * @param keySerializer key serializer
   * @param valueSerializer value serializer
   * @return  an instance of the KafkaWriteStream
   */
  static <K, V> KafkaWriteStream<K, V> create(Vertx vertx, KafkaClientOptions options, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
    Map<String, Object> config = new HashMap<>();
    if (options.getConfig() != null) {
      config.putAll(options.getConfig());
    }
    return new KafkaWriteStreamImpl<>(
      vertx,
      new org.apache.kafka.clients.producer.KafkaProducer<>(config, keySerializer, valueSerializer),
      options);
  }

  /**
   * Create a new KafkaWriteStream instance
   *
   * @param vertx Vert.x instance to use
   * @param producer  native Kafka producer instance
   */
  static <K, V> KafkaWriteStream<K, V> create(Vertx vertx, Producer<K, V> producer) {
    return new KafkaWriteStreamImpl<>(vertx, producer, new KafkaClientOptions());
  }

  @Fluent
  @Override
  KafkaWriteStream<K, V> exceptionHandler(Handler<Throwable> handler);

  @Fluent
  @Override
  KafkaWriteStream<K, V> setWriteQueueMaxSize(int i);

  @Fluent
  @Override
  KafkaWriteStream<K, V> drainHandler(@Nullable Handler<Void> handler);

  /**
   * Initializes the underlying kafka transactional producer. See {@link KafkaProducer#initTransactions()} ()}
   *
   * @return a future notified with the result
   */
  Future<Void> initTransactions();

  /**
   * Starts a new kafka transaction. See {@link KafkaProducer#beginTransaction()}
   *
   * @return a future notified with the result
   */
  Future<Void> beginTransaction();

  /**
   * Commits the ongoing transaction. See {@link KafkaProducer#commitTransaction()}
   *
   * @return a future notified with the result
   */
  Future<Void> commitTransaction();

  /**
   * Aborts the ongoing transaction. See {@link org.apache.kafka.clients.producer.KafkaProducer#abortTransaction()}
   *
   * @return a future notified with the result
   */
  Future<Void> abortTransaction();

  /**
   * Asynchronously write a record to a topic
   *
   * @param record  record to write
   * @return a {@code Future} completed with the record metadata
   */
  Future<RecordMetadata> send(ProducerRecord<K, V> record);

  /**
   * Get the partition metadata for the give topic.
   *
   * @param topic topic partition for which getting partitions info
   * @return a future notified with the result
   */
  Future<List<PartitionInfo>> partitionsFor(String topic);

  /**
   * Invoking this method makes all buffered records immediately available to write
   *
   * @return a future notified with the result
   */
  Future<Void> flush();

  /**
   * Close the stream
   */
  Future<Void> close();

  /**
   * Close the stream
   *
   * @param timeout timeout to wait for closing
   * @return a future notified with the result
   */
  Future<Void> close(long timeout);

  /**
   * @return the underlying producer
   */
  Producer<K, V> unwrap();
}
