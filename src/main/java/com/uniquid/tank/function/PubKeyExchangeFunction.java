/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.tank.function;

import com.uniquid.core.provider.exception.FunctionException;
import com.uniquid.core.provider.impl.GenericFunction;
import com.uniquid.messages.FunctionRequestMessage;
import com.uniquid.messages.FunctionResponseMessage;
import com.uniquid.node.impl.ChannelKey;
import com.uniquid.node.impl.UniquidNodeImpl;
import com.uniquid.register.RegisterFactory;
import com.uniquid.register.exception.RegisterException;
import com.uniquid.register.provider.ProviderChannel;
import com.uniquid.tank.Main;
import org.gmagnotta.log.LogLevel;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;

import static org.gmagnotta.log.LogEventCollector.getInstance;

/**
 * This function is intended to exchange xpub and tpub between User and Provider.
 * User calls this function on provider with User Root Public Key (userXpub) and User Derived Public Key (userTpub) parameters,
 * Provider saves user's xpub and tpub into his providerChannel and responses to user with his xpub and tpub.
 * User receives Provider Root Public Key (providerXpub) and Provider Derived Public Key (providerTpub) in response and saves them in his UserChannel.
 * <p>
 * Contract description:
 * - Doctor device - provider
 * - Patient device - user
 */
public class PubKeyExchangeFunction extends GenericFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class.getName());
    private static final String CONSOLE = "CONSOLE";
    private static final Marker MARKER = MarkerFactory.getMarker(CONSOLE);
    public static final String XPUB = "xpub";
    public static final String TPUB = "tpub";

    private final RegisterFactory registerFactory;
    private final UniquidNodeImpl uniquidNode;

    public PubKeyExchangeFunction(RegisterFactory registerFactory, UniquidNodeImpl uniquidNode) {
        this.registerFactory = registerFactory;
        this.uniquidNode = uniquidNode;

        getInstance().setLogLevelThreshold(LogLevel.DEBUG);
    }

    @Override
    public void service(FunctionRequestMessage inputMessage, FunctionResponseMessage outputMessage, byte[] payload)
            throws FunctionException, IOException {

        LOGGER.info(MARKER, "calling service...");

        // address of patient device
        String userAddress = inputMessage.getUser();

        try {
            ProviderChannel providerChannel = registerFactory.getProviderRegister().getChannelByUserAddress(userAddress);

            // root pub key (xpub)
            String providerRootPubKey = uniquidNode.getPublicKey();
            ChannelKey key = uniquidNode.getChannelKey(providerChannel);

            String params = inputMessage.getParameters();

            JSONObject jsonMessage = new JSONObject(params);
            String userXpub = jsonMessage.getString(XPUB);
            String userTpub = jsonMessage.getString(TPUB);

            providerChannel.setUserXpub(userXpub);
            providerChannel.setUserTpub(userTpub);
            //save to ProviderChannel
            registerFactory.getProviderRegister().updateChannel(providerChannel);

            //derived private key
            String derivedPrivateKey = key.getPrivateKey();
            //derived public key
            String derivedPublicKey = key.getPublicKey();

            JSONObject jo = new JSONObject();
            jo.put(XPUB, providerRootPubKey);
            jo.put(TPUB, derivedPublicKey);
            String outputResult = jo.toString();
            outputMessage.setResult(outputResult);
            LOGGER.info(MARKER, "outputMessage: {}", outputMessage);

        } catch (RegisterException e) {
            LOGGER.error(MARKER, e.getMessage(), e);
        }
    }
}