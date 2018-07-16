package com.umeitime.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.github.mzule.ninegridlayout.ViewGetter;
import com.umeitime.common.tools.GlideUtils;

import java.util.List;

/**
 * Created by CaoDongping on 5/13/16.
 */
public class ImageNineGridView extends AbstractNineGridLayout<List<String>> {
//    private View[] gifViews;
    private MultiImageView.OnItemClickListener mOnItemClickListener;

    public ImageNineGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void fill() {
        fill(new ViewGetter() {
            @Override
            public View getView(int position) {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }
        });
//        imageViews = findInChildren(R.id.ivCover);
//        gifViews = findInChildren(R.id.gif, View.class);

    }

    @Override
    public void render(List<String> images, int itemWidth, int itemHeight) {
        setSingleModeSize(itemWidth, itemHeight);
        setDisplayCount(images.size());
        for (int i = 0; i < images.size(); i++) {
            String url = images.get(i);
            GlideUtils.loadImage(getContext(), url, getImageViews().get(i));
            getImageViews().get(i).setOnClickListener(new ImageOnClickListener(i));
//            gifViews[i].setVisibility(url.endsWith(".gif") ? VISIBLE : INVISIBLE);
        }
    }
    public void setOnItemClickListener(MultiImageView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ImageOnClickListener implements View.OnClickListener {

        private int position;

        public ImageOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, position);
            }
        }
    }
}