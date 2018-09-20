package com.kbs.consume.deduction;

import com.kbs.consume.operations.IOperationsDeduction;
import com.kbs.consume.operations.OperationsFreeDeduction;
import com.kbs.consume.retail.IRetailDeduction;

public class FreeDeductionFactory implements IDeductionFactory{

	@Override
	public IOperationsDeduction createOperationsDeduction() {
		// TODO Auto-generated method stub
		return new OperationsFreeDeduction();
	}

	@Override
	public IRetailDeduction createRetailDection() {
		// TODO Auto-generated method stub
		return null;
	}

}
