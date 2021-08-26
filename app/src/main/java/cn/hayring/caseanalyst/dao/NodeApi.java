package cn.hayring.caseanalyst.dao;

import java.util.List;

import cn.hayring.caseanalyst.domain.Listable;
import io.reactivex.Observable;

/**
 * @author Hayring
 * @date 2021/8/26
 * @description 结点管理接口
 */
public interface NodeApi<T extends Listable> {
    /**
     * 获取结点列表
     *
     * @param caseId 案件id
     * @return 结点列表
     */
    Observable<List<T>> getNodeList(Long caseId);

    /**
     * 创建结点
     *
     * @param caseId      案件id
     * @param newNodeName 新结点名称
     * @return 结点id
     */
    Observable<Long> createNewNode(Long caseId, String newNodeName);

    /**
     * 删除案件
     *
     * @param caseId 案件id
     * @param nodeId 结点id
     * @return 删除成功
     */
    Observable<Boolean> deleteNode(Long caseId, Long nodeId);

    /**
     * 更新案件
     *
     * @param caseId 案件id
     * @param node   结点
     * @return 更新成功
     */
    Observable<Boolean> updateNode(Long caseId, T node);

}
