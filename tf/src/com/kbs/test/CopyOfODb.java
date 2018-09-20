package com.kbs.test;

import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.util.DateTimeUtil;

public class CopyOfODb {
	private MetaDataBO metaDataBO = new MetaDataBO();
	public static void main(String[] args) {
		CopyOfODb odb = new CopyOfODb();
		odb.addcharge();
	}
	public void addcharge(){
		for(int i=0;i<40000;i++){
			System.out.println(i);
			metaDataBO.execute("INSERT INTO `tf-ism`.`tf_member_less_record` (`LESS_ID`, `CREATETIME`, `ACCOUNT_ID`, `LESS_TYPE`, `LESS_MONEY`, `ACCOUNT_OLD_BALANCE`, `ACCOUNT_CUR_BALANCE`, `UPDATETIME`, `ISM_STATUS`, `MI_ID`, `U_ID`, `LESS_STATUS`, `LESS_REMARKS`, `LESS_ADDR`, `STORE_CODE`, `CLIENT_CODE`) VALUES ('17111015100001510645461"+i+"', '"+DateTimeUtil.getNowDate()+"', '171110151000015106454492991493', '3', '10', '0.0', '-10.0', '2017-11-14 15:44:21', '0', '171110151000015106454492981241', '0', '0', '卡工本费', NULL, '0000', '171110151')");
		}
		
	}
}
