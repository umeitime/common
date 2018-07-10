package com.umeitime.common.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.EmptySignature;
import com.umeitime.common.R;
import com.umeitime.common.helper.glide.transformations.CropCircleTransformation;
import com.umeitime.common.helper.glide.transformations.GlideRoundTransform;
import com.umeitime.common.tools.glide.OriginalKey;
import com.umeitime.common.tools.glide.SafeKeyGenerator;
import com.umeitime.common.views.CircleImageView;

import java.io.File;
import java.io.IOException;

public class GlideUtils {
    /**
     * Glide特点
     * 使用简单
     * 可配置度高，自适应程度高
     * 支持常见图片格式 Jpg png gif webp
     * 支持多种数据源  网络、本地、资源、Assets 等
     * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
     * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
     * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
     * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
     */
    //默认加载
    public static void loadImage(Context mContext, int res, ImageView mImageView) {
        Glide.with(mContext).load(res).placeholder(R.drawable.default_pic).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(mImageView);
    }
    //默认加载
    public static void loadImage(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asBitmap().placeholder(R.drawable.default_pic).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(mImageView);
    }

    //加载sd卡
    public static void loadImage(Context mContext, String picPath, ImageView mImageView, int width, int height) {
        Glide.with(mContext).load(picPath).asBitmap().placeholder(R.drawable.default_pic).diskCacheStrategy(DiskCacheStrategy.SOURCE).override(width,height).into(mImageView);
    }

    //默认加载
    public static void loadImage(Context mContext, int icon, ImageView mImageView, int dp) {
        Glide.with(mContext).load(icon).transform(new CenterCrop(mContext),new GlideRoundTransform(mContext, DisplayUtils.dip2px(mContext,dp))).crossFade().into(mImageView);
    }

    //默认加载
    public static void loadImage(Context mContext, int icon, ImageView mImageView, int dp, GlideRoundTransform.CornerType type) {
        Glide.with(mContext).load(icon).transform(new CenterCrop(mContext),new GlideRoundTransform(mContext, DisplayUtils.dip2px(mContext,dp), 0, type)).crossFade().into(mImageView);
    }

    //默认加载指定圆角
    public static void loadImage(Context mContext, String path, ImageView mImageView, int dp) {
        Glide.with(mContext).load(path).placeholder(R.drawable.default_pic).transform(new CenterCrop(mContext),new GlideRoundTransform(mContext, DisplayUtils.dip2px(mContext,dp))).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(mImageView);
    }


    //默认加载指定圆角
    public static void loadFileImageView(Context mContext, String path, ImageView mImageView, int dp) {
        Glide.with(mContext).load(new File(path)).placeholder(R.drawable.default_pic).transform(new CenterCrop(mContext),new GlideRoundTransform(mContext, DisplayUtils.dip2px(mContext,dp))).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(mImageView);
    }
    //默认加载指定圆角
    public static void loadImage(Context mContext, String path, ImageView mImageView, int dp, GlideRoundTransform.CornerType type) {
        Glide.with(mContext).load(path).placeholder(R.drawable.default_pic).transform(new CenterCrop(mContext), new GlideRoundTransform(mContext, DisplayUtils.dip2px(mContext,dp), 0, type)).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(mImageView);
    }
    public static void loadAvatarView(Context mContext, String path, ImageView mImageView, int dp) {
        Glide.with(mContext).load(path).placeholder(R.drawable.empty_avatar_user).transform(new CenterCrop(mContext), new GlideRoundTransform(mContext, DisplayUtils.dip2px(mContext,dp))).error(R.drawable.empty_avatar_user).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(mImageView);
    }

    //加载指定大小
    public static void loadImageViewSize(Context mContext, String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext).load(path).override(width, height).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadImageViewLoding(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public static void loadImageViewLodingSize(Context mContext, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).override(width, height).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置跳过内存缓存
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).skipMemoryCache(true).into(mImageView);
    }

    //设置下载优先级
    public static void loadImageViewPriority(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).priority(Priority.NORMAL).into(mImageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mImageView);
    }

    /**
     * api也提供了几个常用的动画：比如crossFade()
     */

    //设置加载动画
    public static void loadImageViewAnim(Context mContext, String path, int anim, ImageView mImageView) {
        Glide.with(mContext).load(path).animate(anim).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */

    //设置动态转换
    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }

    //设置动态GIF加载方式
    public static void loadImageViewDynamicGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asGif().into(mImageView);
    }

    //设置静态GIF加载方式
    public static void loadImageViewStaticGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asBitmap().into(mImageView);
    }

    //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘

    //设置监听请求接口
    public static void loadImageViewListener(Context mContext, String path, ImageView mImageView, RequestListener<String, GlideDrawable> requstlistener) {
        Glide.with(mContext).load(path).listener(requstlistener).into(mImageView);
    }

    //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排

    //设置要加载的内容
    public static void loadImageViewContent(Context mContext, String path, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(mContext).load(path).centerCrop().into(simpleTarget);
    }

    //清理磁盘缓存
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }

    public static void loadAvatarView(Context mContext, String url, ImageView ivAvatar){
        Glide.with(mContext).load(url).placeholder(R.drawable.empty_avatar_user).bitmapTransform(new CropCircleTransformation(mContext)).crossFade().into(ivAvatar);
    }

    public static void loadAvatarView(Context mContext, String url, ImageView ivAvatar, int width, int height){
        Glide.with(mContext).load(url).override(width, height).placeholder(R.drawable.empty_avatar_user).bitmapTransform(new CropCircleTransformation(mContext)).crossFade().into(ivAvatar);
    }

    public static void loadAvatarView(Context mContext, String url, CircleImageView ivAvatar){
        Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ivAvatar.setImageBitmap(resource);
            }
        });
    }
    public static void loadAvatarView(Context mContext, int res, CircleImageView ivAvatar){
        Glide.with(mContext).load(res).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ivAvatar.setImageBitmap(resource);
            }
        });
    }
    public static void loadAvatarView(Context mContext, String url, CircleImageView ivAvatar, int width, int height){
        Glide.with(mContext).load(url).asBitmap().override(width, height).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ivAvatar.setImageBitmap(resource);
            }
        });
    }

    public static File getCacheFile(Context mContext, String url){
        OriginalKey originalKey = new OriginalKey(url, EmptySignature.obtain());
        SafeKeyGenerator safeKeyGenerator = new SafeKeyGenerator();
        String safeKey = safeKeyGenerator.getSafeKey(originalKey);
        try {
            DiskLruCache diskLruCache = DiskLruCache.open(new File(mContext.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR), 1, 1, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
            DiskLruCache.Value value = diskLruCache.get(safeKey);
            if (value != null) {
                return value.getFile(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
