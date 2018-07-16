//package com.umeitime.common.tools;
//import android.content.Context;
//import android.graphics.PointF;
//import android.net.Uri;
//import android.widget.ImageView;
//import com.bumptech.glide.Glide;
//import com.facebook.binaryresource.FileBinaryResource;
//import com.facebook.cache.common.CacheKey;
//import com.facebook.cache.disk.FileCache;
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
//import com.facebook.drawee.controller.ControllerListener;
//import com.facebook.drawee.view.DraweeView;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
//import com.facebook.imagepipeline.common.ResizeOptions;
//import com.facebook.imagepipeline.core.ImagePipelineFactory;
//import com.facebook.imagepipeline.request.ImageRequest;
//import com.facebook.imagepipeline.request.ImageRequestBuilder;
//import com.umeitime.common.R;
//import java.io.File;
///**
// * Created by hujunwei on 16/6/27.
// */
//public class ImageLoadUtil {
//    public static void loadImage(String url, SimpleDraweeView mDraweeView, int width, int height) {
//        if (StringUtils.isBlank(url)) return;
//        ImageRequest request =
//                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
//                        .setResizeOptions(
//                                new ResizeOptions(width, height))
//                        .setProgressiveRenderingEnabled(true)
//                        .build();
//        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
//                .setOldController(mDraweeView.getController())
//                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                .build();
//        mDraweeView.setController(controller);
//    }
//
//    public static void loadImage(String url, SimpleDraweeView mDraweeView, int width, int height, ControllerListener controllerListener) {
//        ImageRequest request =
//                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
//                        .setResizeOptions(
//                                new ResizeOptions(width, height))
//                        .setProgressiveRenderingEnabled(true)
//                        .build();
//        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
//                .setOldController(mDraweeView.getController())
//                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                .setControllerListener(controllerListener)
//                .build();
//        mDraweeView.setController(controller);
//    }
//
//    public static void loadImage(Context context, String url, ImageView imageView, int width, int height) {
//        Glide.with(context)
//                .load(url)
//                .asBitmap()
//                .dontAnimate()
//                .override(width, height)
//                .centerCrop()
//                .placeholder(R.drawable.default_pic)
//                .into(imageView);
//    }
//
//    public static void loadImage(String url, DraweeView mDraweeView) {
//        ImageRequest request =
//                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
//                        .setProgressiveRenderingEnabled(true)
//                        .build();
//        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
//                .setOldController(mDraweeView.getController())
//                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                .build();
//        mDraweeView.setController(controller);
//    }
//    public static void loadImage(String url, SimpleDraweeView mDraweeView, PointF focusPoint) {
//        if(focusPoint==null){
//            focusPoint = new PointF(0.5f,0.5f);
//        }
//        ImageRequest request =
//                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
//                        .setProgressiveRenderingEnabled(true)
//                        .build();
//        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
//                .setOldController(mDraweeView.getController())
//                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                .build();
//        mDraweeView.setController(controller);
//        mDraweeView
//                .getHierarchy()
//                .setActualImageFocusPoint(focusPoint);
//    }
//    public static boolean isImageDownloaded(Uri loadUri) {
//        if (loadUri == null) {
//            return false;
//        }
//        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri),null);
//        return ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey) || ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey);
//    }
//
//    //return file or null
//    public static File getCachedImageOnDisk(Uri loadUri) {
//        ImageRequest request = ImageRequest.fromUri(loadUri);
//        FileCache mainFileCache = ImagePipelineFactory
//                .getInstance()
//                .getMainFileCache();
//        final CacheKey cacheKey = DefaultCacheKeyFactory
//                .getInstance()
//                .getEncodedCacheKey(request, false); // we don't need context, but avoid null
//        File cacheFile = request.getSourceFile();
//        if (mainFileCache.hasKey(cacheKey)) {
//            cacheFile = ((FileBinaryResource) mainFileCache.getResource(cacheKey)).getFile();
//        }
//        return cacheFile;
//    }
//}
