package cn.hayring.caseanalyst.view.caselist;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;

import cn.hayring.caseanalyst.CaseAnalystApplication;
import cn.hayring.caseanalyst.domain.Case;
import cn.hayring.caseanalyst.net.NetworkInterface;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hayring
 * @date 6/21/21 7:26 PM
 */
public class CaseViewModel extends ViewModel {

    public CaseViewModel() {
    }

    /**
     * model 层单例
     */
//    CaseRepository caseModel = CaseModel.getInstance();

    /**
     * 带有案件列表的 livedata
     */
    MutableLiveData<List<Case>> caseListData = new MutableLiveData<>();

    /**
     * 单个案件 livedata
     */
    MutableLiveData<Case> singleCase = new MutableLiveData<>();

    /**
     * 系统生成的案件结点id
     */
    MutableLiveData<Case> newCase = new MutableLiveData<>();

    /**
     * 删除案件反馈
     */
    MutableLiveData<Boolean> deleteResponse = new MutableLiveData<>();

    /**
     * 更新案件反馈
     */
    MutableLiveData<Boolean> updateResponse = new MutableLiveData<>();


    /**
     * 获取案件列表
     */
    public void getCaseList() {
        NetworkInterface.caseApi.getCaseList()
                //反转顺序
                .map(new Function<List<Case>, List<Case>>() {
                    @Override
                    public List<Case> apply(@NonNull List<Case> cases) throws Exception {
                        //反转使最新的在下面
                        Collections.reverse(cases);
                        return cases;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<List<Case>>() {

                    @Override
                    public void onNext(@NonNull List<Case> cases) {
                        caseListData.setValue(cases);
                    }

                });

    }
//
//    private final AsyncCallBack<List<Case>> getCaseListCallback = new AsyncCallBack<List<Case>>() {
//        @Override
//        public void callBack(List<Case> cases) {
//            caseListData.postValue(cases);
//        }
//    };


    /**
     * 删除案件
     *
     * @param caseId 案件 id
     */
    public void deleteCase(Long caseId) {
        NetworkInterface.caseApi.deleteCase(caseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        deleteResponse.setValue(aBoolean);
                    }
                });
    }


    /**
     * 查询单个案件
     *
     * @param id 案件 id
     */
    public void getCase(Long id) {

    }


    /**
     * 更新案件
     *
     * @param caxe 案件
     */
    public void updateCase(Case caxe) {
        NetworkInterface.caseApi.updateCase(caxe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        updateResponse.setValue(aBoolean);
                    }
                });
    }


    private static final String NEW_CASE_DEFAULT_NAME = "新案件";

    public void addCase() {
        NetworkInterface.caseApi.createNewCase(NEW_CASE_DEFAULT_NAME)
                .map(new Function<Long, Case>() {
                    @Override
                    public Case apply(@NonNull Long id) throws Exception {
                        Case caxe = new Case();
                        caxe.setId(id);
                        caxe.setName(NEW_CASE_DEFAULT_NAME);
                        return caxe;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<Case>() {
                    @Override
                    public void onNext(@NonNull Case caxe) {
                        newCase.setValue(caxe);
                    }
                });
    }


    public MutableLiveData<Case> getNewCase() {
        return newCase;
    }

    public LiveData<List<Case>> getCaseListData() {
        return caseListData;
    }

    public MutableLiveData<Case> getSingleCase() {
        return singleCase;
    }

    public MutableLiveData<Boolean> getDeleteResponse() {
        return deleteResponse;
    }

    public MutableLiveData<Boolean> getUpdateResponse() {
        return updateResponse;
    }

    static abstract class MyObserver<T> implements Observer<T> {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }


        @Override
        public void onError(@NonNull Throwable e) {
            Toasty.error(CaseAnalystApplication.getInstance().getApplicationContext(), e.getMessage()).show();
            Log.e(e.toString(), e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    }
}
