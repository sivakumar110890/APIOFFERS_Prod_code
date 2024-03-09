/*
 * COPYRIGHT: Mahindra Comviva Technologies Pvt. Ltd.
 *
 * This software is the sole property of Comviva and is protected
 * by copyright law and international treaty provisions. Unauthorized
 * reproduction or redistribution of this program, or any portion of
 * it may result in severe civil and criminal penalties and will be
 * prosecuted to the maximum extent possible under the law.
 * Comviva reserves all rights not expressly granted. You may not
 * reverse engineer, decompile, or disassemble the software, except
 * and only to the extent that such activity is expressly permitted
 * by applicable law notwithstanding this limitation.
 *
 * THIS SOFTWARE IS PROVIDED TO YOU "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE. YOU ASSUME THE ENTIRE RISK AS TO THE ACCURACY
 * AND THE USE OF THIS SOFTWARE. Comviva SHALL NOT BE LIABLE FOR
 * ANY DAMAGES WHATSOEVER ARISING OUT OF THE USE OF OR INABILITY TO
 * USE THIS SOFTWARE, EVEN IF Comviva HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.comviva.api;

import com.comviva.api.conf.ConfConstants;
import com.comviva.api.conf.ConfLoader;
import com.comviva.api.exception.ConfigException;
import com.comviva.api.j4u.service.consumer.ConsumerService;
import com.comviva.api.j4u.service.consumer.IConsumerService;
import com.comviva.api.j4u.service.publisher.J4UOfferEventPublisher;
import com.comviva.api.j4u.utils.ProductInfoCache;
import com.comviva.api.j4u.utils.TemplateCache;
import com.comviva.voltdb.factory.DAOFactory;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class InitializeListener implements ServletContextListener {
    public static final Logger LOGGER = Logger.getLogger(InitializeListener.class);

    private static String userDir = System.getProperty("user.dir");
    private static String PROPERTIES_FILENAME = userDir + "/config/";

    // Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        LOGGER.info("Initializing Configuration & Starting all consumers");

        try {
            loadConfigurations(PROPERTIES_FILENAME);
            // caching j4u offer

            LOGGER.info("ProductInfoCache Started.");
            ProductInfoCache.instance();

            LOGGER.info("ProductInfoCache loaded.");

            TemplateCache.instance();

            LOGGER.info("TemplateCache loaded.");

            // j4u kafka event producer
            J4UOfferEventPublisher.startPublisher();

            // j4u kafka event consumer
            IConsumerService consumerService = new ConsumerService();
            consumerService.startConsumer();

            LOGGER.info("Consumer & publisher started.");
        } catch (ConfigException e) {
            LOGGER.error("Error initializing the Configuration File", e);
        } catch (Exception e) {
            LOGGER.error("Error..", e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            DAOFactory.close();
            J4UOfferEventPublisher.stopExecutorThread();

            // j4u kafka event consumer
            IConsumerService consumerService = new ConsumerService();
            consumerService.stopConsumer();

            LOGGER.info("Consumer & publisher stopped.");

        } catch (Exception e) {
            LOGGER.error("Error in contextDestroyed", e);
        }
    }

    /**
     * This method loads the configuration parameters from configuration file
     * and initialize the configuration reload thread.
     *
     * @param confPath
     * @throws ConfigException
     */
    private void loadConfigurations(String confPath) throws ConfigException {
        ConfLoader confLoader = new ConfLoader(confPath + ConfConstants.CONF_FILE_NAME);
        confLoader.loadConfigurations();
        LOGGER.fatal("Configuration Loaded");

    }

}