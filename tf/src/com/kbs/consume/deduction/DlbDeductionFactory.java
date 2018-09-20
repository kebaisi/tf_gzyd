package com.kbs.consume.deduction;

import com.kbs.consume.operations.IOperationsDeduction;
import com.kbs.consume.operations.OperationsDlbDeduction;
import com.kbs.consume.operations.OperationsZfbDeduction;
import com.kbs.consume.retail.IRetailDeduction;
import com.kbs.consume.retail.RetailCashDeduction;

public class DlbDeductionFactory implements IDeductionFactory {

	@Override
	public IOperationsDeduction createOperationsDeduction() {
		// TODO Auto-generated method stub
		return new OperationsDlbDeduction();
	}

	@Override
	public IRetailDeduction createRetailDection() {
		// TODO Auto-generated method stub
		return new RetailCashDeduction();
	}

}
