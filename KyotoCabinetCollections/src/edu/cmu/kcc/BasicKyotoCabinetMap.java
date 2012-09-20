package edu.cmu.kcc;

import java.io.IOException;

public class BasicKyotoCabinetMap extends KyotoCabinetMap<String, String> {

	public BasicKyotoCabinetMap(String filename, char mode) throws IOException {
		super(filename, mode);
	}

	@Override
	public String unmakeKey(String key) {
		return key;
	}

	@Override
	public String makeKey(Object key) {
		return (String) key;
	}

	@Override
	public String unmakeValue(String value) {
		return value;
	}

	@Override
	public String makeValue(String value) {
		return value;
	}
	
}
