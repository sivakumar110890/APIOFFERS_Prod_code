package com.comviva.api.j4u.service.consumer;

import com.comviva.api.j4u.service.QueryBalanceService;
import com.emagine.kafka.consumer.EventConsumer;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import static com.comviva.api.j4u.utils.J4UOfferConstants.MPESA_QUARY_BAL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SOURCE;

/**
 * Topic subscriber class listens to topic and processes the messages.
 */
public class J4UOfferConsumer extends EventConsumer {

    private static final Logger LOGGER = Logger.getLogger(J4UOfferConsumer.class);

    public J4UOfferConsumer(String groupId, String topic, int threads) {
        super(groupId, topic, threads);
    }

    @Override
    public void onReceive(String message, int threadId) {
        LOGGER.info("onRecieve method :: [Start] - [Thread Id]=[" + threadId + "] Message ==> " + message);
        try {
            JSONObject jsonObject = new JSONObject(message);
            QueryBalanceService offerService = new QueryBalanceService();
            if (jsonObject.has(SOURCE) && !jsonObject.getString(SOURCE).equalsIgnoreCase(MPESA_QUARY_BAL)) {
                LOGGER.debug("calling processActivateOffer...");
                offerService.processActivateOffer(jsonObject);
            } else {
                //"SOURCE":"MPESA_QUARY_BAL"
                LOGGER.debug("calling processQueryBalanceRequest...");
                offerService.processQueryBalanceRequest(jsonObject);
            }
            LOGGER.info("onRecieve method :: [END] - [Thread Id]=[" + threadId + "]");
        } catch (Exception e) {

            LOGGER.error("onRecieve method :: [ERROR OCCURED]  thread Id =[" + threadId + "] :: Message = "
                            + e.getMessage());
        }
    }
}
