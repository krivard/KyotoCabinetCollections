package edu.cmu.kcc;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import edu.cmu.kcc.BasicKyotoCabinetMap;
import edu.cmu.kcc.KyotoCabinetMap;

public class KyotoCabinetMapTest {
	private String[][] answers = {
			{"birds","bees"},
			{"coffee","tea"},
			{"catsup","mustard"},
			{"peanut butter","jelly"}
	};
	
	@Test
	public void testPutGet() throws IOException {
		File mapfile = File.createTempFile("kccmap-", ".kch"); mapfile.deleteOnExit();
		KyotoCabinetMap<String,String> map = new BasicKyotoCabinetMap(mapfile.getAbsolutePath(),'w');
		for (String[] pair:answers) {
			map.put(pair[0], pair[1]);
		}
		for (String[] pair:answers) {
			assertEquals(pair[0],pair[1],map.get(pair[0]));
		}
	}
	
	@Test
	public void testPutIterkeys() throws IOException {
		File mapfile = File.createTempFile("kccmap-", ".kch"); mapfile.deleteOnExit();
		KyotoCabinetMap<String,String> map = new BasicKyotoCabinetMap(mapfile.getAbsolutePath(),'w');
		Set<String> keys = new TreeSet<String>();
		for (String[] pair:answers) {
			map.put(pair[0], pair[1]);
			keys.add(pair[0]);
		}
		for (String key : map.keySet()) {
			assertTrue(key,keys.contains(key));
			keys.remove(key);
		}
		assertEquals(keys.size()+" keys left!", 0,keys.size());
	}
	@Test
	public void testPutItervalues() throws IOException {
		File mapfile = File.createTempFile("kccmap-", ".kch"); mapfile.deleteOnExit();
		KyotoCabinetMap<String,String> map = new BasicKyotoCabinetMap(mapfile.getAbsolutePath(),'w');
		Set<String> values = new TreeSet<String>();
		for (String[] pair:answers) {
			map.put(pair[0], pair[1]);
			values.add(pair[1]);
		}
		for (String value : map.values()) {
			assertTrue(value,values.contains(value));
			values.remove(value);
		}
		assertEquals(values.size()+" keys left!", 0,values.size());
	}
	@Test
	public void testPutIterentries() throws IOException {
		File mapfile = File.createTempFile("kccmap-", ".kch"); mapfile.deleteOnExit();
		KyotoCabinetMap<String,String> map = new BasicKyotoCabinetMap(mapfile.getAbsolutePath(),'w');
		Set<String> values = new TreeSet<String>();
		for (String[] pair:answers) {
			map.put(pair[0], pair[1]);
			values.add(pair[0]);
		}
		for (Map.Entry<String, String> value : map.entrySet()) {
			assertTrue(value.getKey(),values.contains(value.getKey()));
			values.remove(value.getKey());
		}
		assertEquals(values.size()+" entries left!", 0,values.size());
	}
}
