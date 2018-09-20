package com.kbs.test;

import com.kbs.consume.operations.IOperationsValuation;
import com.kbs.consume.retail.IRetailValuation;
import com.kbs.consume.valuation.AutoValuationFactory;
import com.kbs.consume.valuation.IValuationFactory;
import com.kbs.consume.valuation.ManuallyValuationFactory;

public class TestConsume {
	public static void main(String[] args) {
		IValuationFactory valuationFactory = new ManuallyValuationFactory();
		IOperationsValuation auto = valuationFactory.createOperationsValuation();
		IRetailValuation manm = valuationFactory.createRetailValuation();
		manm.valuation();
	}
}
