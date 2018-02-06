package com.noticeditorteam.noticeditorandroid;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import java.util.ArrayList;
import java.util.Collection;

public class NoticeTreeAdapter extends RecyclerView.Adapter<NoticeTreeAdapter.ViewHolder> {
    private ArrayList<NoticeItem> notices;
    private OnNoticeClickListener onItemClickListener;
    private AppCompatActivity context;

    public MultiChoiceHelper getHelper() {
        return helper;
    }

    private MultiChoiceHelper helper;

    public AppCompatActivity getContext() {
        return context;
    }

    public static class ViewHolder extends MultiChoiceHelper.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        RelativeLayout mRelativeLayout;

        ViewHolder(View v) {
            super(v);
            mImageView = v.findViewById(R.id.image_view);
            mTextView = v.findViewById(R.id.text_view);
            mRelativeLayout = v.findViewById(R.id.view_layout);
        }

        public RelativeLayout getRelativeLayout() {
            return mRelativeLayout;
        }
    }

    public NoticeTreeAdapter(AppCompatActivity context, ArrayList<NoticeItem> noteset,
                             OnNoticeClickListener listener) {
        this.context = context;
        notices = noteset;
        onItemClickListener = listener;
        helper = new MultiChoiceHelper(context, this);
    }

    @Override
    public NoticeTreeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_text_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoticeTreeAdapter.ViewHolder holder, int position) {
        onItemClickListener.setCurrentNotice(notices.get(position));
        holder.mTextView.setText(notices.get(position).getTitle());
        int imageId;
        if(notices.get(position).isBranch()) {
            imageId = R.drawable.nnf_ic_folder_black_48dp;
        }
        else {
            imageId = R.drawable.ic_file_document_black_48dp;
        }
        holder.mImageView.setImageResource(imageId);
        holder.mImageView.setColorFilter(fetchAccentColor());
        holder.bind(helper, position);
        holder.setOnClickListener(onItemClickListener);
    }

    public void add(NoticeItem notice) {
        notices.add(notice);
    }

    public void addAll(Collection<NoticeItem> notes) {
        notices.addAll(notes);
    }

    public void set(int index, NoticeItem notice) { notices.set(index, notice); }

    public void clear() {
        notices.clear();
    }

    public void remove(NoticeItem noticeItem) { notices.remove(noticeItem); }

    public NoticeItem getItem(int position) {
        return notices.get(position);
    }

    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public void setModeListener(MultiChoiceHelper.MultiChoiceModeListener modeListener) {
        helper.setMultiChoiceModeListener(modeListener);
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    public abstract static class OnNoticeClickListener implements RecyclerView.OnClickListener {
        private NoticeItem currentNotice;
        private AppCompatActivity context;

        public void setCurrentNotice(NoticeItem notice) {
            currentNotice = notice;
        }

        public NoticeItem getCurrentNotice() {
            return currentNotice;
        }

        public AppCompatActivity getContext() { return context; }

        public void setContext(AppCompatActivity context) { this.context = context; }
    }
}
