package mblog.core.persist.service;

import mblog.core.data.Notify;
import mtons.modules.pojos.Paging;

/**
 * @author cdq2016/8/31.
 */
public interface NotifyService {

    void findByOwnId(Paging paging, long ownId);

    /**
     * 发送通知
     * @param notify
     */
    void send(Notify notify);

    /**
     * 未读消息数量
     * @param ownId
     * @return
     */
    int unread4Me(long ownId);

    /**
     * 标记为已读
     * @param ownId
     */
    void readed4Me(long ownId);

}
