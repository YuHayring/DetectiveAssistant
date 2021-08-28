package cn.hayring.caseanalyst.view.casemanager.nodelistpager;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.hayring.caseanalyst.domain.Listable;
import cn.hayring.caseanalyst.view.AbstractListAdapter;
import cn.hayring.caseanalyst.view.ListableViewHolder;
import cn.hayring.caseanalyst.view.MyListActivity;
import cn.hayring.caseanalyst.view.casemanager.nodevaluesetter.NodeValueSetter;
import cn.hayring.caseanalyst.view.listener.RecyclerItemDeleteDialogListener;

/***
 * 案件列表设置器
 * @author Hayring
 */
public class JetpackFragmentListAdapter<T extends Listable> extends AbstractListAdapter<T> {
    /***
     * Fragment引用
     */
    private JetpackListFragment<T> fragment;

    /***
     * 输入数据源构造器
     * @param items
     */
    public JetpackFragmentListAdapter(List<T> items, JetpackListFragment<T> fragment) {
        super(items);
        this.fragment = fragment;
    }

    /**
     * 绑定监听器
     *
     * @param holder
     */
    @Override
    public void bindListener(ListableViewHolder holder) {
        holder.itemView.setOnClickListener(editItemListener);
        deleteItemListener = new DeleteItemListener(fragment.requireContext());
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
            Listable node = items.get(position);

            //注册Activity，NodeValueSetter
            Intent itemTransporter = new Intent(fragment.requireContext(), fragment.getValueSetterClass());
            //绑定参数
            itemTransporter.putExtra(NodeValueSetter.CREATE_OR_NOT, false);
            itemTransporter.putExtra(NodeValueSetter.INTENT_KEY_NODE, node);
            itemTransporter.putExtra(NodeValueSetter.INDEX, position);

            //启动NodeValueSetter
            fragment.startActivityForResult(itemTransporter, MyListActivity.REQUEST_CODE);


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
            fragment.getViewModel().deleteNode(fragment.getCaseId(), id);
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
        v = LayoutInflater.from(fragment.requireContext()).inflate(fragment.getSingleLayoutId(), parent, false);
        return new ListableViewHolder(v);
    }

    @Deprecated
    public void setItem(int index, T node) {
        items.set(index, node);
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
