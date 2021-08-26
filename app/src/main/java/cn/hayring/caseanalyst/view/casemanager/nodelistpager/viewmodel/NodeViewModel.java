package cn.hayring.caseanalyst.view.casemanager.nodelistpager.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;

import cn.hayring.caseanalyst.CaseAnalystApplication;
import cn.hayring.caseanalyst.dao.NodeApi;
import cn.hayring.caseanalyst.domain.Listable;
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
public abstract class NodeViewModel<T extends Listable> extends ViewModel {

    public NodeViewModel() {
    }

    public abstract T newInstance(Long id, String name);

    public abstract String getDefaultNodeName();

    protected abstract NodeApi<T> getNodeApi();


    /**
     * 带有结点列表的 livedata
     */
    MutableLiveData<List<T>> nodeListData = new MutableLiveData<>();

    /**
     * 单个结点 livedata
     */
    MutableLiveData<T> singleNode = new MutableLiveData<>();

    /**
     * 系统生成的结点结点id
     */
    MutableLiveData<T> newNode = new MutableLiveData<>();

    /**
     * 删除结点反馈
     */
    MutableLiveData<Boolean> deleteResponse = new MutableLiveData<>();

    /**
     * 更新结点反馈
     */
    MutableLiveData<Boolean> updateResponse = new MutableLiveData<>();


    /**
     * 获取结点列表
     *
     * @param caseId 案件id
     */
    public void getNodeList(Long caseId) {
        getNodeApi().getNodeList(caseId)
                //反转顺序
                .map(new Function<List<T>, List<T>>() {
                    @Override
                    public List<T> apply(@NonNull List<T> nodes) throws Exception {
                        //反转使最新的在下面
                        Collections.reverse(nodes);
                        return nodes;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<List<T>>() {

                    @Override
                    public void onNext(@NonNull List<T> nodes) {
                        nodeListData.setValue(nodes);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        nodeListData.setValue(null);
                    }
                });

    }


    /**
     * 删除结点
     *
     * @param caseId 案件id
     * @param nodeId 结点 id
     */
    public void deleteNode(Long caseId, Long nodeId) {
        getNodeApi().deleteNode(caseId, nodeId)
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
     * 更新结点
     *
     * @param caseId 案件id
     * @param node   结点
     */
    public void updateNode(Long caseId, T node) {
        getNodeApi().updateNode(caseId, node)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        updateResponse.setValue(aBoolean);
                    }
                });
    }


    /**
     * 添加结点
     *
     * @param caseId 案件id
     */
    public void addNode(Long caseId) {
        getNodeApi().createNewNode(caseId, getDefaultNodeName())
                .map(new Function<Long, T>() {
                    @Override
                    public T apply(@NonNull Long id) throws Exception {
                        return newInstance(id, getDefaultNodeName());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<T>() {
                    @Override
                    public void onNext(@NonNull T node) {
                        newNode.setValue(node);
                    }
                });
    }


    public MutableLiveData<T> getNewNode() {
        return newNode;
    }

    public LiveData<List<T>> getNodeListData() {
        return nodeListData;
    }

    public MutableLiveData<T> getSingleNode() {
        return singleNode;
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
