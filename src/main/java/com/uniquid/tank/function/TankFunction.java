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
import com.uniquid.node.impl.UniquidNodeImpl;
import com.uniquid.register.RegisterFactory;
import com.uniquid.register.impl.sql.SQLiteRegisterFactory;
import com.uniquid.register.provider.ProviderChannel;
import com.uniquid.tank.entity.Tank;

import java.io.IOException;

public class TankFunction extends GenericFunction {

	// constructor accepts registerFactory and uniquidNode

	/*RegisterFactory registerFactory = new SQLiteRegisterFactory(appSettings.getDBUrl());
	registerFactory.get*/

	@Override
	public void service(FunctionRequestMessage inputMessage, FunctionResponseMessage outputMessage, byte[] payload)
			throws FunctionException, IOException {

		/*RegisterFactory registerFactory = new SQLiteRegisterFactory(appSettings.getDBUrl());
		String userAddress = inputMessage.getUser();
		ProviderChannel providerChannel = registerFactory.getProviderRegister().getChannelByUserAddress(userAddress);
		final UniquidNodeImpl uniquidNode = new UniquidNodeImpl();
		// root pub key (xpub)
		String publicKey = uniquidNode.getPublicKey();
		ChannelKey key = uniquidNode.getChannelKey(providerChannel);*/

		// "; Private key: " + key.getPrivateKey() +
		// derived pub key
		// 		"; Public key: " + key.getPublicKey());


		Tank tank = Tank.getInstance();



		String params = inputMessage.getParameters();
		String result = "";
		if (params.startsWith("open")) {
			
			tank.open();
			
			result = "\nOpening Machine\n-- Level " + tank.getLevel() + " in faucet = " + booleanToInt(tank.isInputOpen()) + " out faucet = " +  booleanToInt(tank.isOutputOpen()) + "\n";
			
		} else if (params.startsWith("close")) {
			
			tank.close();
			
			result = "\nClosing Machine\n-- Level " + tank.getLevel() + " in faucet = " + booleanToInt(tank.isInputOpen()) + " out faucet = " +  booleanToInt(tank.isOutputOpen()) + "\n";
			
		}
		
		outputMessage.setResult(result);
		
	}
	
	private static int booleanToInt(final boolean open) {
		if (open) {
			return 1;
		} else {
			return 0;
		}
		
	}

}