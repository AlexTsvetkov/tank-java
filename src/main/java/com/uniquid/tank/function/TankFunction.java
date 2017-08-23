package com.uniquid.tank.function;

import java.io.IOException;

import com.uniquid.core.ProviderRequest;
import com.uniquid.core.ProviderResponse;
import com.uniquid.core.provider.exception.FunctionException;
import com.uniquid.core.provider.impl.GenericFunction;
import com.uniquid.tank.entity.Tank;

public class TankFunction extends GenericFunction {

	@Override
	public void service(ProviderRequest inputMessage, ProviderResponse outputMessage, byte[] payload)
			throws FunctionException, IOException {
		
		Tank tank = Tank.getInstance();
		
		String params = inputMessage.getParams();
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