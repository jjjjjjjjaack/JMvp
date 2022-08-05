package framework.mvp1.base.util;//package framework.mvp1.base.util;
//
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.view.View;
//
//
//import java.io.File;
//
//import framework.mvp1.base.net.API_Factory;
//import framework.mvp1.base.net.BaseResponse;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Action0;
//import rx.functions.Func1;
//import rx.schedulers.Schedulers;
//
//
///**
// * 用于检测更新
// */
//public class CheckUpdatesVersionUtil {
//
//    private static CheckUpdatesVersionUtil util;
//
//    //同步代码快的demo加锁，安全高效
//    public static CheckUpdatesVersionUtil getInstance() {
//        if (util == null)
//            synchronized (CheckUpdatesVersionUtil.class) {
//                if (util == null)
//                    util = new CheckUpdatesVersionUtil();
//            }
//        return util;
//    }
//
//    final String ERRO = "-1";
//    final String DONOT_UPDATE = "0";
//    final String CAN_UPDATE = "1";
//    final String HAVETO_UPDATE = "2";
//    private PopupVersionSelectView popupVersionSelectView;
//
//    /**
//     * 获取服务器版本信息
//     */
//    public void doVersionCheck(Context mContext, final View parentView, final PopupVersionSelectView.ISelectUpdate iSelectUpdate) {
//        if (popupVersionSelectView != null) {
//            popupVersionSelectView.dismiss();
//            popupVersionSelectView = null;
//        }
//        popupVersionSelectView = new PopupVersionSelectView((Activity) mContext, iSelectUpdate);
//        API_Factory.ANN_API_POST_DOCOMM(new GET_APPVERSION_REQ()).doOnSubscribe(new Action0() {
//            @Override
//            public void call() {
//            }
//        }).subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.io()).map(new Func1<BaseResponse, FVersionBean>() {
//            @Override
//            public FVersionBean call(BaseResponse baseResponse) {
//
//                FVersionBean versionEntity = new FVersionBean();
//                try {
//                    versionEntity.fromJSONAuto(baseResponse.datas);
//                    versionEntity.versionCode = versionEntity.versionName;
//                } catch (Exception e) {
//                    versionEntity.setState(ERRO);
//                    return versionEntity;
//                }
//
//
//                if (!versionEntity.versionControl) {
//                    // 若不控制更新，则直接进入
//                    versionEntity.setState(DONOT_UPDATE);
//                    return versionEntity;
//                }
//
//                String versionName = ToolUtils.isNull(versionEntity.versionName) ? "" : versionEntity.versionName;
//                String versionChangelog = versionEntity.changeLog;
//                final long apkTotalSize = ToolUtils.String2Long(versionEntity.size);//apk文件大小
//                final boolean constraint = versionEntity.constraint;// 是否强制更新
//                final String installUrl = versionEntity.downloadLink;
//
//                popupVersionSelectView.setContentMsg(versionChangelog);
//                popupVersionSelectView.setDownLoadUrl(installUrl);
//                // 得到当前已安装的app版本信息
//                String currentVersionName = "";
//                PackageManager manager = MyApplication.getApp().getPackageManager();
//                PackageInfo info = null;
//                try {
//                    info = manager.getPackageInfo(MyApplication.getApp().getPackageName(), 0);
//                    if (info == null) {
//                        //获取失败则直接进入
//                        versionEntity.setState(ERRO);
//                        return versionEntity;
//                    } else {
//                        currentVersionName = (info.versionName + "").trim();
//                    }
//                } catch (PackageManager.NameNotFoundException e) {
//                    //获取失败则直接进入
//                    versionEntity.setState(ERRO);
//                    return versionEntity;
//                }
//                // 比较两者
//                if (currentVersionName.equals(versionName)) {
//                    //若从服务器得到的版本号不等于当前版app版本号，则不弹出更新，直接进入
//                    versionEntity.setState(DONOT_UPDATE);
//                    return versionEntity;
//                }
//                //是否需要强制更新
//                if (!constraint) {
//                    versionEntity.setState(CAN_UPDATE);
//                    return versionEntity;
//                } else {
//                    versionEntity.setState(HAVETO_UPDATE);
//                    return versionEntity;
//                }
//
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<FVersionBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        ToolUtils.doNetErroMsg(MyApplication.getInstance2().getActivity(), e, true, false);
//                        iSelectUpdate.erro();
//                    }
//
//                    @Override
//                    public void onNext(FVersionBean versionEntity) {
//                        switch (versionEntity.state) {
//                            case ERRO:
//                                iSelectUpdate.erro();
//                                break;
//                            case DONOT_UPDATE:
//                                iSelectUpdate.doNotUpdate();
//                                break;
//                            case CAN_UPDATE:
//                                popupVersionSelectView.showPop(parentView);
//                                break;
//                            case HAVETO_UPDATE:
//                                popupVersionSelectView.showPop(parentView);
//                                //如果constraint参数为true，则要强制更新
//                                popupVersionSelectView.displayConstrantUpdate();
//                                break;
//                        }
//                    }
//                });
//    }
//
//    public void readyDownload(final Context mContext) {
//        final AlertDialog.Builder wifiBuilder = new AlertDialog.Builder(
//                mContext);
//        wifiBuilder.setPositiveButton(
//                mContext.getResources().getString(
//                        R.string.yes),
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(
//                            DialogInterface dialog,
//                            int which) {
//                        downloadApp(mContext);
//                    }
//                });
////        wifiBuilder.setNegativeButton(
////                MyApplication.getInstance2().getContext().getResources().getString(
////                        R.string.no),
////                new DialogInterface.OnClickListener() {
////
////                    @Override
////                    public void onClick(
////                            DialogInterface dialog,
////                            int which) {
////                        //直接进入
////                    }
////                });
//        wifiBuilder.setMessage(MyApplication.getApp().getResources()
//                .getString(R.string.wifitip));
//        wifiBuilder.create().show();
//    }
//
//    /**
//     * 根据网址用浏览器打开
//     */
//    public void downloadApp(Context mContext) {
//        final ProgressDialog dialog = new ProgressDialog(mContext);
//        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setMessage("安装包下载中");
//        dialog.setMax(100);
//        dialog.show();
//        DownloadUtil downloadUtil = DownloadUtil.get();
//        downloadUtil.download(popupVersionSelectView.downLoadUrl, "", "apkdownload", new DownloadUtil.OnDownloadListener() {
//            @Override
//            public void onDownloadSuccess(File file) {
//                ((Activity) mContext).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.dismiss();
//                        T.showShort(mContext, "下载完成,正打开安装程序");
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////                            Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
//                            Uri uri = FileProviderUtil.getUri(mContext, file);
//                            intent.setDataAndType(uri, "application/vnd.android.package-archive");
//                        } else {
//                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                        }
////                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                        mContext.startActivity(intent);
//                    }
//                });
//            }
//
//            @Override
//            public void onDownloading(int progress) {
//                dialog.setProgress(progress);
//            }
//
//            @Override
//            public void onDownloadFailed() {
//                ((Activity) mContext).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.dismiss();
//                        T.showShort(mContext, "下载失败，尝试用浏览器打开下载");
//                        Intent intent = new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        intent.setData(Uri.parse(popupVersionSelectView.downLoadUrl));
//                        mContext.startActivity(intent);
//                    }
//                });
//            }
//        });
//
////        Intent intent = new Intent();
////        intent.setAction("android.intent.action.VIEW");
////        intent.setData(Uri.parse(popupVersionSelectView.downLoadUrl));
////        mContext.startActivity(intent);
//
////        T.showLong(MyApplication.getInstance2().getContext(), MyApplication.getInstance2().getContext().getResources().getString(R.string.splashact_select_explore));
//    }
//
//
//}
