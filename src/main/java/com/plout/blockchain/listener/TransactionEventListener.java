package com.plout.blockchain.listener;

import com.plout.blockchain.core.Transaction;
import com.plout.blockchain.event.NewTransactionEvent;
import com.plout.blockchain.utils.SerializeUtils;
import com.plout.blockchain.net.base.MessagePacket;
import com.plout.blockchain.net.base.MessagePacketType;
import com.plout.blockchain.net.client.AppClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 发送交易事件监听器
 * @author yangjian
 * @since 18-4-19
 */
@Component
public class TransactionEventListener {

	@Autowired
	private AppClient appClient;
	private static Logger logger = LoggerFactory.getLogger(TransactionEventListener.class);

	/**
	 * 向所有客户端广播交易
	 * @param event
	 */
	@EventListener(NewTransactionEvent.class)
	public void sendTransaction(NewTransactionEvent event)
	{

		logger.info("++++++++++++++ 开始广播新新的交易订单 +++++++++++++++++++++");
		Transaction transaction = (Transaction) event.getSource();
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_CONFIRM_TRANSACTION);
		messagePacket.setBody(SerializeUtils.serialize(transaction));
		appClient.sendGroup(messagePacket);
	}

}
