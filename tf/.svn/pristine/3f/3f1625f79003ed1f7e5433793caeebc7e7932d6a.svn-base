package com.kbs.consume.deduction;

import com.kbs.consume.operations.IOperationsDeduction;
import com.kbs.consume.operations.IOperationsValuation;
import com.kbs.consume.operations.OperationsCashDeduction;
import com.kbs.consume.operations.OperationsWxDeduction;
import com.kbs.consume.retail.IRetailDeduction;
import com.kbs.consume.retail.IRetailValuation;
import com.kbs.consume.retail.RetailCashDeduction;

public class WxDeductionFactory implements IDeductionFactory {

	@Override
	public IOperationsDeduction createOperationsDeduction() {
		// TODO Auto-generated method stub
		return new OperationsWxDeduction();
	}

	@Override
	public IRetailDeduction createRetailDection() {
		// TODO Auto-generated method stub
		return new RetailCashDeduction();
	}

}
