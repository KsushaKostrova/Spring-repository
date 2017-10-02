package com.kostrova;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestGood {

	@Test
	public void testGetId() {
		Good good = new Good();
		good.setId(1);
		Integer actual = good.getId();
		assertTrue(actual.equals(1));
	}

	@Test
	public void testSetId() {
		Good good = new Good();
		good.setId(1);
		Integer actual = good.getId();
		assertTrue(actual.equals(1));
	}

	@Test
	public void testGetName() {
		Good good = new Good();
		good.setName("hi");
		String actual = good.getName();
		assertTrue(actual.equals("hi"));
	}

	@Test
	public void testEquals() {
		Good good1 = new Good();
		Good good2 = new Good();
		assertTrue(good1.equals(good2));
	}

	@Test
	public void testHashCode() {
		Integer id = 1;
		Good good = new Good();
		good.setId(id);
		int hashCode = 31 + ((id == null) ? 0 : id.hashCode());
		assertTrue(good.hashCode() == (hashCode));
	}
}
