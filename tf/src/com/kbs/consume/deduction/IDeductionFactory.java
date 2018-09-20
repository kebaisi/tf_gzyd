package com.kbs.consume.deduction;

import com.kbs.consume.operations.IOperationsDeduction;
import com.kbs.consume.operations.IOperationsValuation;
import com.kbs.consume.retail.IRetailDeduction;
import com.kbs.consume.retail.IRetailValuation;

public interface IDeductionFactory {
	IOperationsDeduction createOperationsDeduction();
	IRetailDeduction createRetailDection();
}
