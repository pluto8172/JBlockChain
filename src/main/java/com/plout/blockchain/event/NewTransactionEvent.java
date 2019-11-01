package com.plout.blockchain.event;

import com.plout.blockchain.core.Transaction;
import org.springframework.context.ApplicationEvent;

/**
 * 发送交易事件
 * @author yangjian
 */
public class NewTransactionEvent extends ApplicationEvent {

    public NewTransactionEvent(Transaction transaction) {
        super(transaction);
    }

}
