package com.comviva.api.j4u.service.consumer;

import com.comviva.api.exception.ConfigException;
import com.comviva.api.j4u.config.PropertiesLoader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.comviva.api.j4u.utils.J4UOfferConstants.MPESA_CONSUMER_TOPIC;

public class ConsumerService implements IConsumerService {

    private static final Logger LOGGER = Logger.getLogger(ConsumerService.class);

    private static List<J4UOfferConsumer> consumerList = new ArrayList<J4UOfferConsumer>();

    public String startConsumer() throws  ConfigException{
        try {
            LOGGER.info("startConsumer method :: [Start]");
            String mpesaTopic = PropertiesLoader.getValue(MPESA_CONSUMER_TOPIC);
            String[] topicList = mpesaTopic.split("~");
            for (int i = 0; i < topicList.length; i++) {
                String topicArray = topicList[i];
                String[] topicDetails = topicArray.split(":");
                String topic = topicDetails[0];
                String groupId = topicDetails[1];
                int threadCount = Integer.parseInt(topicDetails[2]);
                J4UOfferConsumer j4uOfferConsumer = new J4UOfferConsumer(groupId, topic, threadCount);
                try {
                    j4uOfferConsumer.startConsumer();
                } catch (Exception e) {
             LOGGER.error("error", e);
                }
                consumerList.add(j4uOfferConsumer);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            try {
                throw new Exception("INCORRECT Configuration in properties for mpesa.topic ");
            } catch (Exception e) {
                LOGGER.error("error", e);
            }
        }
        return "SUCCESS";
    }

    public String stopConsumer() throws ConfigException {
        LOGGER.info("stopConsumer method :: [Start]");
        boolean isConsumerNull = false;
        if (consumerList != null) {
            for (J4UOfferConsumer j4uOfferConsumer : consumerList) {
                if (j4uOfferConsumer != null) {
                    j4uOfferConsumer.shutdownAllConsumers();
                } else {
                    isConsumerNull = true;
                }
            }
            if (isConsumerNull) {
                try {
                    forceStopAllConsumers();
                } catch (Exception e) {
                    LOGGER.error("error", e);
                }
            }
        } else {
            try {
                forceStopAllConsumers();
            } catch (Exception e) {
                LOGGER.error("error", e);
        
            }
        }
        LOGGER.info("stopConsumer method :: [End]");
        return "SUCCESS";
    }

    private void forceStopAllConsumers() throws Exception {
        try {
            /*
             * Stop all consumers as we are not sure which one has been garbage
             * collected.
             */
            String mpesaTopic = PropertiesLoader.getValue(MPESA_CONSUMER_TOPIC);
            String[] topicList = mpesaTopic.split("~");
            for (int i = 0; i < topicList.length; i++) {
                String topicArray = topicList[i];
                String[] topicDetails = topicArray.split(":");
                String topic = topicDetails[0];
                String groupId = topicDetails[1];
                int threadCount = Integer.getInteger(topicDetails[2]).intValue();
                J4UOfferConsumer j4uOfferConsumer = new J4UOfferConsumer(groupId, topic, threadCount);
                j4uOfferConsumer.shutdownAllConsumers();
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ConfigException("INCORRECT Configuration in properties for mpesa.topic ");
        }
    }

}
