package com.kbs.consume.deduction;

import com.kbs.consume.operations.IOperationsDeduction;
import com.kbs.consume.operations.IOperationsValuation;
import com.kbs.consume.operations.OperationsFaceDeduction;
import com.kbs.consume.operations.OperationsIcDeduction;
import com.kbs.consume.retail.IRetailDeduction;
import com.kbs.consume.retail.IRetailValuation;
import com.kbs.consume.retail.RetailIcDeduction;

public class FaceDeductionFactory implements IDeductionFactory {

	@Override
	public IOperationsDeduction createOperationsDeduction() {
		// TODO Auto-generated method stub
		return new OperationsFaceDeduction();
	}

	@Override
	public IRetailDeduction createRetailDection() {
		// TODO Auto-generated method stub
		return new RetailIcDeduction();
	}

}
