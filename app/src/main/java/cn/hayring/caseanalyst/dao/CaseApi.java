package cn.hayring.caseanalyst.dao;

import java.util.List;

import cn.hayring.caseanalyst.domain.Case;
import io.reactivex.Observable;

/**
 * @author Hayring
 * @date 2021/8/25
 * @description 案件管理接口
 */
public interface CaseApi {

    /**
     * 获取案件
     *
     * @return
     */
    Observable<List<Case>> getCaseList();


    /**
     * 创建案件
     */
    Observable<Long> createNewCase(String newCaseName);

    /**
     * 删除案件
     */
    Observable<Boolean> deleteCase(Long caseId);

    /**
     * 更新案件
     */
    Observable<Boolean> updateCase(Case caxe);

}
