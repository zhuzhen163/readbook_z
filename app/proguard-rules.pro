# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-ignorewarnings                     # 忽略警告，避免打包时某些警告出现
-optimizationpasses 5               # 指定代码的压缩级别
-dontusemixedcaseclassnames         # 是否使用大小写混合 混淆时不会产生形形色色的类名
-dontskipnonpubliclibraryclasses    # 是否混淆第三方jar
-dontpreverify                      # 混淆时是否做预校验
-verbose                            # 混淆时是否记录日志
-dontoptimize                       # 不优化输入的类文件

-keepattributes *Annotation*, SourceFile, InnerClasses, LineNumberTable, Signature, EnclosingMethod
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    #优化 混淆时采用的算法

-keep public class * extends android.app.Activity    # 未指定成员，仅仅保持类名不被混淆
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.View
-keep public class * extends android.app.IntentService
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.hardware.display.DisplayManager
-keep public class * extends android.os.UserManager
-keep public class * extends android.app.Fragment

-keep public class * extends android.support.v4.widget
-keep public class * extends android.support.v4.**    #  *匹配任意字符不包括.  **匹配任意字符
-keep interface android.support.v4.app.** { *; }    #{ *;}    表示一个类中的所有的东西
-keep class android.support.v4.** { *; }        # 保持一个完整的包不被混淆
-keep class android.os.**{*;}

-keep class **.R$* { *; }
-keep class **.R{ *; }

#实现了android.os.Parcelable接口类的任何类，以及其内部定义的Creator内部类类型的public final静态成员变量，都不能被混淆和删除
-keep class * implements android.os.Parcelable {    # 保持Parcelable不被混淆
  public static final android.os.Parcelable$Creator *;
}

-keepclasseswithmembernames class * {     # 保持 native 方法不被混淆
    native <methods>;
}
#保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity { #保持类成员
   public void *(android.view.View);
}

-keepclassmembers class * extends android.content.Context {
  public void *(android.view.View);
  public void *(android.view.MenuItem);
}
# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
  static final long serialVersionUID;
  private static final java.io.ObjectStreamField[] serialPersistentFields;
  private void writeObject(java.io.ObjectOutputStream);
  private void readObject(java.io.ObjectInputStream);
  java.lang.Object writeReplace();
  java.lang.Object readResolve();
}


#对于带有回调函数的onXXListener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Listener);
}

#-libraryjars   libs/treecore.jar   #缺省proguard 会检查每一个引用是否正确，但是第三方库里面往往有些不会用到的类，没有正确引用。如果不配置的话，系统就会报错。
-dontwarn android.support.v4.**
-dontwarn android.os.**

##############################--------以上是Android基本配置----------##############################

# 实体类不混淆（自己项目实体类不被混淆，因为Gson中用到了反射）
-keep class com.huajie.readbook.bean.** { *; }
-keep class com.huajie.readbook.db.** { *; }
# OkHttp3
 -dontwarn com.squareup.okhttp3.**
 -keep class com.squareup.okhttp3.** { *;}
 -dontwarn okio.**
 # Okio
 -dontwarn com.squareup.**
 -dontwarn okio.**
 -keep public class org.codehaus.* { *; }
 -keep public class java.nio.* { *; }
#retrofit rxjava okhttp混淆
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#极光
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#腾讯buly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#友盟分享混淆
 -dontshrink
    -dontoptimize
    -dontwarn com.google.android.maps.**
    -dontwarn android.webkit.WebView
    -dontwarn com.umeng.**
    -dontwarn com.tencent.weibo.sdk.**
    -keep public class javax.**
    -keep public class android.webkit.**
    -dontwarn android.support.v4.**
    -keepattributes Exceptions,InnerClasses,Signature
    -keepattributes *Annotation*
    -keepattributes SourceFile,LineNumberTable

    -keep public interface com.tencent.**
    -keep public interface com.umeng.socialize.**
    -keep public interface com.umeng.socialize.sensor.**
    -keep public interface com.umeng.scrshot.**
    -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
    -keep public class com.umeng.socialize.* {*;}
    -keep class com.umeng.scrshot.**
    -keep public class com.tencent.** {*;}
    -keep class com.umeng.socialize.sensor.**
    -keep class com.umeng.socialize.handler.**
    -keep class com.umeng.socialize.handler.*
    -keep class com.umeng.weixin.handler.**
    -keep class com.umeng.weixin.handler.*
    -keep class com.umeng.qq.handler.**
    -keep class com.umeng.qq.handler.*
    -keep class UMMoreHandler{*;}
    -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
    -keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
    -keep class com.tencent.mm.sdk.** {
     *;
    }
    -keep class com.tencent.mm.opensdk.** {
   *;
    }

    -keep class com.tencent.** {*;}
    -dontwarn com.tencent.**
    -keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
    }
    -keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
        }
    -keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    }

# talkingdata
    -keep class com.tencent.open.TDialog$*
    -keep class com.tencent.open.TDialog$* {*;}
    -keep class com.tencent.open.PKDialog
    -keep class com.tencent.open.PKDialog {*;}
    -keep class com.tencent.open.PKDialog$*
    -keep class com.tencent.open.PKDialog$* {*;}

    -keep class com.sina.** {*;}
    -dontwarn com.sina.**
    -keep class  com.alipay.share.sdk.** {
       *;
    }
    -keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
    }

    -keep class com.linkedin.** { *; }
    -keepattributes Signature

    -dontwarn com.tendcloud.tenddata.**
    -keep class com.tendcloud.** {*;}
    -keep public class com.tendcloud.tenddata.** { public protected *;}
    -keepclassmembers class com.tendcloud.tenddata.**{
    public void *(***);
    }
    -keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
    -keep class com.apptalkingdata.** {*;}
    -keep class dice.** {*; }
    -dontwarn dice.**

    #glide
    -keep public class * implements com.bumptech.glide.module.GlideModule
