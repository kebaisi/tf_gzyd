package com.kbs.consume.operations;

import com.kbs.consume.MyException;
import com.kbs.consume.valuation.AutoValuationFactory;
import com.kbs.consume.valuation.IValuationFactory;
import com.kbs.util.TF;

public class RfidThread extends Thread{
	private IValuationFactory valuationFactory=new AutoValuationFactory();
	private IOperationsValuation valuation=valuationFactory.createOperationsValuation();
//	public static boolean isOnSale=false;
	@Override
	public synchronized void run() {
		echo:while(true){
			if(TF.comStatusMap.getIntValue("rfidComStatus")==1){
				if(TF.currentMealTimes.equals("0") || TF.ConsumeJson.getIntValue("stage_type")==2){
				
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue echo;
				}
				try {
//					if(TF.comStatusMap.getIntValue("rfidComStatus")==1){
						valuation.valuation();
//					}
				} catch (MyException e) {
					continue echo;
				}
//			}else{
//				System.out.println("RFID is sleep......");
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}else{
				try {
					Thread.sleep(50000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
