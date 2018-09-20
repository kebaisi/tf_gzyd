package com.kbs.test;

import java.math.BigDecimal;

import com.kbs.util.DataConvertUtil;

public class Test2 {
	public static void main(String[] args) {
		BigDecimal a = new BigDecimal(5.5);
		BigDecimal b = new BigDecimal(10.6);
		System.out.println(DataConvertUtil.moneryFormat(b.subtract(a).doubleValue()));
		System.out.println(a.subtract(b));
	}
}
