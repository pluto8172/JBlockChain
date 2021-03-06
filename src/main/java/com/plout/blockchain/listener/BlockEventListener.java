package com.plout.blockchain.listener;

import com.google.common.base.Optional;
import com.plout.blockchain.core.Block;
import com.plout.blockchain.event.BlockConfirmNumEvent;
import com.plout.blockchain.event.FetchNextBlockEvent;
import com.plout.blockchain.event.NewBlockEvent;
import com.plout.blockchain.utils.SerializeUtils;
import com.plout.blockchain.db.DBAccess;
import com.plout.blockchain.net.base.MessagePacket;
import com.plout.blockchain.net.base.MessagePacketType;
import com.plout.blockchain.net.client.AppClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 区块事件监听器
 * @author yangjian
 * @since 18-4-19
 */
@Component
public class BlockEventListener {

	@Autowired
	private AppClient appClient;
	@Autowired
	private DBAccess dbAccess;
	private static Logger logger = LoggerFactory.getLogger(BlockEventListener.class);

	/**
	 * 挖矿事件监听
	 * @param event
	 */
	@EventListener(NewBlockEvent.class)
	public void newBlock(NewBlockEvent event) {

		logger.info("++++++++++++++ 开始广播新区块 +++++++++++++++++++++");
		Block block = (Block) event.getSource();
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_NEW_BLOCK);
		messagePacket.setBody(SerializeUtils.serialize(block));
		appClient.sendGroup(messagePacket);
	}

	/**
	 * 同步下一个区块
	 * @param event
	 */
	@EventListener(FetchNextBlockEvent.class)
	public void fetchNextBlock(FetchNextBlockEvent event) {

		logger.info("++++++++++++++++++++++++++++++ 开始群发信息获取 next Block +++++++++++++++++++++++++++++++++");
		Integer blockIndex = (Integer) event.getSource();
		if (blockIndex == 0) {
			Optional<Object> lastBlockIndex = dbAccess.getLastBlockIndex();
			if (lastBlockIndex.isPresent()) {
				blockIndex = (Integer) lastBlockIndex.get();
			}
		}
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_SYNC_NEXT_BLOCK);
		messagePacket.setBody(SerializeUtils.serialize(blockIndex+1));
		//群发消息，从群组节点去获取下一个区块
		appClient.sendGroup(messagePacket);
	}

	@EventListener(BlockConfirmNumEvent.class)
	public void IncBlockConfirmNum(BlockConfirmNumEvent event)
	{
		logger.info("++++++++++++++ 增加区块确认数 ++++++++++++++++++");
		Integer blockIndex = (Integer) event.getSource();
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_INC_CONFIRM_NUM);
		messagePacket.setBody(SerializeUtils.serialize(blockIndex));
		//群发消息
		appClient.sendGroup(messagePacket);
	}

}
