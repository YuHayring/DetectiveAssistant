package cn.hayring.caseanalyst.view.caselist;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.hayring.caseanalyst.domain.Case;
import cn.hayring.caseanalyst.view.AbstractListAdapter;
import cn.hayring.caseanalyst.view.ListableViewHolder;
import cn.hayring.caseanalyst.view.MyListActivity;
import cn.hayring.caseanalyst.view.ValueSetter;
import cn.hayring.caseanalyst.view.listener.RecyclerItemDeleteDialogListener;

/***
 * 案件列表设置器
 * @author Hayring
 */
public class CaseListAdapter extends AbstractListAdapter<Case> {


    /***
     * Activity引用
     */
    private CaseListActivity mActivity;

    /***
     * 输入数据源构造器
     * @param items
     */
    public CaseListAdapter(CaseListActivity mActivity, List<Case> items) {
        super(items);
        this.mActivity = mActivity;
        deleteItemListener = new DeleteItemListener(mActivity);
    }

    /**
     * 绑定监听器
     *
     * @param holder
     */
    @Override
    public void bindListener(ListableViewHolder holder) {
        holder.itemView.setOnClickListener(editItemListener);
        holder.itemView.setOnLongClickListener(deleteItemListener);
    }

    EditItemListener editItemListener = new EditItemListener();

    /***
     * 元素编辑点击监听器
     */
    public class EditItemListener implements View.OnClickListener {


        /***
         * 编辑元素
         * Edit Item
         * @param view
         */
        @Override
        public void onClick(View view) {
            //取出元素id
            int position = ((RecyclerView) view.getParent()).getChildAdapterPosition(view);
            Case caxe = items.get(position);

            //注册Activity，ValueSetter
            Intent itemTransporter = new Intent(mActivity, mActivity.getValueSetterClass());
            //绑定参数
            itemTransporter.putExtra(ValueSetter.CREATE_OR_NOT, false);
            itemTransporter.putExtra(ValueSetter.CASE, caxe);
            itemTransporter.putExtra(ValueSetter.INDEX, position);

            //启动ValueSetter
            mActivity.startActivityForResult(itemTransporter, MyListActivity.REQUEST_CODE);


        }
    }


    DeleteItemListener deleteItemListener;

    int lastDeleteIndex;

    /***
     * 元素删除监听器
     */
    class DeleteItemListener extends RecyclerItemDeleteDialogListener {

        public DeleteItemListener(Context context) {
            super(context);
        }

        @Override
        protected void delete(int index) {
            lastDeleteIndex = index;
            long id = items.get(index).getId();
            mActivity.getCaseViewModel().deleteCase(id);
        }
    }


    /***
     *
     * @return count 总数
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /***
     * Layout绑定
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ListableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //注册单个元素的layout
        View v;
        v = LayoutInflater.from(mActivity).inflate(mActivity.getSingleLayoutId(), parent, false);
        return new ListableViewHolder(v);
    }

    @Deprecated
    public void setItem(int index, Case caxe) {
        items.set(index, caxe);
        notifyItemChanged(index);
    }

    /**
     * 确认删除上次尝试删除的元素
     */
    public void deleteLastTryDeleteItem() {
        deleteItem(lastDeleteIndex);
        lastDeleteIndex = -1;
    }


}
