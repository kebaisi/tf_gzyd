package com.kbs.consume.deduction;

import com.kbs.consume.operations.IOperationsDeduction;
import com.kbs.consume.operations.OperationsZfbDeduction;
import com.kbs.consume.retail.IRetailDeduction;
import com.kbs.consume.retail.RetailCashDeduction;

public class ZfbDeductionFactory implements IDeductionFactory {

	@Override
	public IOperationsDeduction createOperationsDeduction() {
		// TODO Auto-generated method stub
		return new OperationsZfbDeduction();
	}

	@Override
	public IRetailDeduction createRetailDection() {
		// TODO Auto-generated method stub
		return new RetailCashDeduction();
	}

}
