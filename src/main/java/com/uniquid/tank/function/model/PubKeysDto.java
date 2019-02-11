package com.uniquid.tank.function.model;

public class PubKeysDto {

	private String xpub;
	private String tpub;

	public PubKeysDto() {
	}

	public PubKeysDto(String xpub, String tpub) {
		this.xpub = xpub;
		this.tpub = tpub;
	}

	public String getXpub() {
		return xpub;
	}

	public String getTpub() {
		return tpub;
	}
}