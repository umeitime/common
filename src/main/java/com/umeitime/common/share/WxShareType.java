package com.umeitime.common.share;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.umeitime.common.share.WxShareType.type_image;
import static com.umeitime.common.share.WxShareType.type_miniProgram;
import static com.umeitime.common.share.WxShareType.type_music;
import static com.umeitime.common.share.WxShareType.type_text;
import static com.umeitime.common.share.WxShareType.type_video;
import static com.umeitime.common.share.WxShareType.type_webPage;

@IntDef({ type_text, type_image, type_video, type_music, type_webPage, type_miniProgram })//枚举类型
@Retention(RetentionPolicy.SOURCE) //告诉编译器在生成.class文件时不保留枚举注解数据
public @interface WxShareType {
  int type_text = 0;
  int type_image = 1;
  int type_video = 2;
  int type_music = 3;
  int type_webPage = 4;
  int type_miniProgram = 5;
}