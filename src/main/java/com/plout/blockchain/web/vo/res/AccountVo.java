package com.plout.blockchain.web.vo.res;

import com.plout.blockchain.account.Account;

/**
 * account VO
 * @author yangjian
 * @since 18-7-14
 */
public class AccountVo extends Account {

	@Override
	public String toString() {
		return "AccountVo{" +
				"address='" + address + '\'' +
				", priKey='" + priKey + '\'' +
				'}';
	}
}
