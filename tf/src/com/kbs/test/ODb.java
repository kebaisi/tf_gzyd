package com.kbs.test;

import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.util.DateTimeUtil;

public class ODb {
	private MetaDataBO metaDataBO = new MetaDataBO();
	public static void main(String[] args) {
		ODb odb = new ODb();
		odb.addcharge();
	}
	public void addcharge(){
		for(int i=0;i<40000;i++){
			System.out.println(i);
			metaDataBO.execute("INSERT INTO `tf_member_recharge_record` (`RECHARGE_ID`, `CREATETIME`, `ACCOUNT_ID`, `RECHARGE_TYPE`, `RECHARGE_MONEY`, `ACCOUNT_OLD_BALANCE`, `ACCOUNT_CUR_BALANCE`, `UPDATETIME`, `ISM_STATUS`, `MI_ID`, `U_ID`, `RECHARGE_STATUS`, `RECHARGE_REMARKS`, `RECHARGE_ADDR`, `STORE_CODE`, `CLIENT_CODE`) VALUES ('17111015100001510640575"+i+"', '"+DateTimeUtil.getNowDate()+"', '171110151000015106394857271652', '0', '"+i+"', '7.0', '100007.0', '2017-11-14 14:22:55', NULL, '171110151000015106394857261764', '', '0', '充值', '', '0000', '171110151')");
		}
		
	}
}
