package com.plout.blockchain.web.controller.api;

import com.google.common.base.Optional;
import com.plout.blockchain.account.Account;
import com.plout.blockchain.account.Personal;
import com.plout.blockchain.crypto.ECKeyPair;
import com.plout.blockchain.crypto.Keys;
import com.plout.blockchain.db.DBAccess;
import com.plout.blockchain.utils.JsonVo;
import com.plout.blockchain.web.vo.res.AccountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yangjian
 * @since 18-4-8
 */
@RestController
@RequestMapping("/api/account")
@Api(tags = "Account API", description = "账户相关的 API")
public class AccountController {

	@Autowired
	private Personal personal;

	@Autowired
	private DBAccess dbAccess;

	/**
	 * 创建账户
	 * @return
	 */
	@ApiOperation(value="创建一个新的钱包账户")
	@GetMapping("/new_account")
	public JsonVo newAccount() throws Exception
	{
		ECKeyPair keyPair = Keys.createEcKeyPair();
		Account account = personal.newAccount(keyPair);
		AccountVo vo = new AccountVo();
		BeanUtils.copyProperties(account, vo);
		return new JsonVo(JsonVo.CODE_SUCCESS, "New account created, please remember your Address and Private Key.", vo);
	}

	/**
	 * 获取挖矿账号
	 * @return
	 */
	@ApiOperation(value="获取挖矿钱包账号", notes = "获取挖矿钱包账号信息，包括地址，私钥，余额等信息")
	@GetMapping("/get_miner_address")
	public JsonVo getMinerAddress() {
		Optional<Account> minerAccount = dbAccess.getMinerAccount();
		JsonVo success = JsonVo.success();
		if (minerAccount.isPresent()) {
			success.setItem(minerAccount.get());
		} else {
			success.setMessage("Miner account is not created");
		}
		return success;
	}

	/**
	 * 列出所有的账号
	 * @return
	 */
	@GetMapping("/list")
	@ApiOperation(value = "获取当前节点所有钱包账户")
	public JsonVo getAllAccounts()
	{
		List<Account> accounts = dbAccess.getAllAccounts();
		JsonVo success = JsonVo.success();
		success.setItem(accounts);
		return success;
	}
}
