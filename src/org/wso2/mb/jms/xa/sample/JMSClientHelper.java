/*
 * Copyright (c) 2005-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.mb.jms.xa.sample;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSClientHelper {
    /**
     * Full qualified class name of the andes initial context factory
     */
    public static final String ANDES_INITIAL_CONTEXT_FACTORY = "org.wso2.andes.jndi" +
                                                               ".PropertiesFileInitialContextFactory";

    /**
     * Queue connection factory name used
     */
    public static final String QUEUE_CONNECTION_FACTORY = "andesQueueConnectionfactory";

    /**
     * Topic connection factory name used
     */
    public static final String TOPIC_CONNECTION_FACTORY = "andesTopicConnectionfactory";

    /**
     * Create a inital context with the given parameters
     *
     * @param username
     *         Username
     * @param password
     *         Password
     * @param brokerHost
     *         Hostname or IP address of the broker
     * @param port
     *         Port used for AMQP transport
     * @param queueName
     *         Queue name
     * @return InitialContext
     * @throws NamingException
     */
    public static InitialContext getInitialContextForQueue(String username,
                                                           String password,
                                                           String brokerHost,
                                                           String port,
                                                           String queueName) throws NamingException {

        Properties contextProperties = new Properties();
        contextProperties.put(Context.INITIAL_CONTEXT_FACTORY, ANDES_INITIAL_CONTEXT_FACTORY);
        String connectionString = getBrokerConnectionString(username, password, brokerHost, port);
        contextProperties.put("connectionfactory." + QUEUE_CONNECTION_FACTORY, connectionString);
        contextProperties.put("queue." + queueName, queueName);

        return new InitialContext(contextProperties);
    }

    /**
     * Create a inital context with the given parameters
     *
     * @param username
     *         Username
     * @param password
     *         Password
     * @param brokerHost1
     *         Hostname or IP address of the broker
     * @param port1
     *         Port used for AMQP transport
     * @param queueName
     *         Queue name
     * @return InitialContext
     * @throws NamingException
     */
    public static InitialContext getInitialContextForQueueWithFailover(String username,
                                                                       String password,
                                                                       String brokerHost1,
                                                                       String port1,
                                                                       String brokerHost2,
                                                                       String port2,
                                                                       String queueName) throws NamingException {

        Properties contextProperties = new Properties();
        contextProperties.put(Context.INITIAL_CONTEXT_FACTORY, ANDES_INITIAL_CONTEXT_FACTORY);
        String connectionString = getFailoverBrokerConnectionString(username, password, brokerHost1, port1, brokerHost2, port2);
        contextProperties.put("connectionfactory." + QUEUE_CONNECTION_FACTORY, connectionString);
        contextProperties.put("queue." + queueName, queueName);

        return new InitialContext(contextProperties);
    }

    /**
     * Create a inital context with the given parameters
     *
     * @param username
     *         Username
     * @param password
     *         Password
     * @param brokerHost
     *         Hostname or IP address of the broker
     * @param port
     *         Port used for AMQP transport
     * @param queueName
     *         Queue name
     * @return InitialContext
     * @throws NamingException
     */
    public static InitialContext getInitialContextForQueueSSL(String username,
                                                           String password,
                                                           String brokerHost,
                                                           String port,
                                                           String queueName) throws NamingException {

        Properties contextProperties = new Properties();
        contextProperties.put(Context.INITIAL_CONTEXT_FACTORY, ANDES_INITIAL_CONTEXT_FACTORY);
        String connectionString = getBrokerSSLConnectionString(username, password, brokerHost, port);
        contextProperties.put("connectionfactory." + QUEUE_CONNECTION_FACTORY, connectionString);
        contextProperties.put("queue." + queueName, queueName);

        return new InitialContext(contextProperties);
    }
    /**
     * Create a inital context with the given parameters
     *
     * @param username
     *         Username
     * @param password
     *         Password
     * @param brokerHost
     *         Hostname or IP address of the broker
     * @param port
     *         Port used for AMQP transport
     * @param topicName
     *         Topic name
     * @return InitialContext
     * @throws NamingException
     */
    public static InitialContext getInitialContextForTopic(String username,
                                                           String password,
                                                           String brokerHost,
                                                           String port,
                                                           String topicName) throws NamingException {

        Properties contextProperties = new Properties();
        contextProperties.put(Context.INITIAL_CONTEXT_FACTORY, ANDES_INITIAL_CONTEXT_FACTORY);
        String connectionString = getBrokerConnectionString(username, password, brokerHost, port);
        contextProperties.put("connectionfactory." + TOPIC_CONNECTION_FACTORY, connectionString);
        contextProperties.put("topic." + topicName, topicName);

        return new InitialContext(contextProperties);
    }

    public static InitialContext getInitialContextForTopicWithFailover(String username,
                                                                       String password,
                                                                       String brokerHost1,
                                                                       String port1,
                                                                       String brokerHost2,
                                                                       String port2,
                                                                       String topicName) throws NamingException {

        Properties contextProperties = new Properties();
        contextProperties.put(Context.INITIAL_CONTEXT_FACTORY, ANDES_INITIAL_CONTEXT_FACTORY);
        String connectionString = getFailoverBrokerConnectionString(username, password, brokerHost1, port1, brokerHost2, port2);
        contextProperties.put("connectionfactory." + TOPIC_CONNECTION_FACTORY, connectionString);
        contextProperties.put("topic." + topicName, topicName);

        return new InitialContext(contextProperties);
    }

    /**
     * Generate broker connection string
     *
     * @param userName
     *         Username
     * @param password
     *         Password
     * @param brokerHost
     *         Hostname of broker (E.g. localhost)
     * @param port
     *         Port (E.g. 5672)
     * @return Broker Connection String
     */
    private static String getBrokerConnectionString(String userName, String password,
                                                    String brokerHost, String port) {

        return "amqp://" + userName + ":" + password + "@clientID/carbon?brokerlist='tcp://"
               + brokerHost + ":" + port + "'";
    }

    /**
     * Generate broker connection string with failover
     *
     * @param userName
     *         Username
     * @param password
     *         Password
     * @param brokerHost1
     *         Hostname of broker (E.g. localhost)
     * @param port1
     *         Port (E.g. 5672)
     * @return Broker Connection String
     */
    private static String getFailoverBrokerConnectionString(String userName, String password,
                                                    String brokerHost1, String port1, String brokerHost2, String port2) {

        return "amqp://" + userName + ":" + password + "@clientID/carbon?brokerlist='"
               + "tcp://" + brokerHost1 + ":" + port1 + "?connectdelay='1000'&connecttimeout='3000'&retries='3';"
               + "tcp://" + brokerHost2 + ":" + port2 + "?connectdelay='1000'&connecttimeout='3000'&retries='3';"
               + "'";
    }

    /**
     * Generate broker SSL connection string
     *
     * @param userName
     *         Username
     * @param password
     *         Password
     * @param brokerHost
     *         Hostname of broker (E.g. localhost)
     * @param port
     *         Port (E.g. 5672)
     * @return Broker Connection String
     */
    private static String getBrokerSSLConnectionString(String userName, String password,
                                                    String brokerHost, String port) {

        return "amqp://" + userName + ":" + password + "@clientID/carbon?brokerlist='tcp://"
               + brokerHost + ":" + port
               + "?ssl='true'&trust_store='conf/sample-client-truststore"
               + ".jks'&trust_store_password='user123'&key_store='conf/clientkeystore.jks'&key_store_password='user123''";
    }
}
