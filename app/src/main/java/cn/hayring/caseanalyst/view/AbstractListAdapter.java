package cn.hayring.caseanalyst.view;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.hayring.caseanalyst.domain.Listable;

/**
 * @author hayring
 * @date 6/22/21 4:39 PM
 */
public abstract class AbstractListAdapter<T extends Listable> extends RecyclerView.Adapter<ListableViewHolder> {
    /***
     * 名字显示长度限制
     */
    public static final int NAME_CHAR_LENGTH = 11;

    /***
     * 信息显示长度限制
     */
    public static final int INFO_CHAR_LENGTH = 16;


    /***
     * 元素集合，显示数据源
     */
    protected List<T> items;

    /***
     * 数据源getter
     */
    public List<T> getItems() {
        return items;
    }

    public AbstractListAdapter() {
    }

    public AbstractListAdapter(List<T> items) {
        this.items = items;
    }

    /***
     * 显示内容的绑定
     * Bind data and view；
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ListableViewHolder holder, int position) {
        //缩减并绑定名称
        String name = items.get(position).getName();
        if (name.length() > NAME_CHAR_LENGTH) {
            name = name.substring(0, NAME_CHAR_LENGTH - 2) + "...";
        }
        holder.name.setText(name);

        //缩减并绑定信息绑定
        String info = items.get(position).getInfo();
        if (info == null) info = "";
        if (info.length() > INFO_CHAR_LENGTH) {
            info = info.substring(0, INFO_CHAR_LENGTH - 2) + "...";
        }
        //TODO
        if (holder.info != null) holder.info.setText(info);

        //注册点击监听器
        bindListener(holder);
    }

    /**
     * 注册监听器
     *
     * @param holder
     */
    public abstract void bindListener(ListableViewHolder holder);

    //改变数据源的四个方法


    public void addItem(T item) {
        items.add(item);
        ////更新数据集不是用adapter.notifyDataSetChanged()而是notifyItemInserted(position)与notifyItemRemoved(position)
        // 否则没有动画效果。
        notifyItemInserted(items.size() - 1);
    }

    public void setItem(int position, T item) {
        items.set(position, item);
        notifyItemChanged(position);
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void addAllItem(List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void deleteAll() {
        this.items.clear();
        notifyDataSetChanged();
    }


}
