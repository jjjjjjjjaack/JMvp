package framework.mvp1.base.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;


import com.jjjjjjjjaack.jmvp.R;
import com.jjjjjjjjaack.jmvp.app.MyApplication;
import com.jjjjjjjjaack.jmvp.app.net.REQ_Factory;

import java.io.File;

import framework.mvp1.base.bean.BaseBean;
import framework.mvp1.base.f.BasePresent;
import framework.mvp1.base.net.BaseResponse;
import framework.mvp1.views.pop.PopupVersionSelectView;

public class CheckVersionUtils {

    private static CheckVersionUtils checkVersionUtil;
    public static Integer key = new Integer(2);//锁

    private CheckVersionUtils() {
    }

    public static CheckVersionUtils getInstance() {
        //先判断该user变量是否为空，入股为空，进入同步代码块，该步假设为step1
        if (checkVersionUtil == null) {    //step 1     //想象一下，如果不判断，那么每次访问这个方法不管该对象是否已经创建都要进入同步代码块，线程数一多，资源消耗也是非常巨大的。
            synchronized (key) {
                //由于可能多个线程都进入了step1,由于锁定机制，一个线程进入该代码块时，其他线程
                //仍在排队进入该代码块，如果不做判断，当前线程即使创造了实例，下一个线程也不知道，就会继续创建一个实例
                if (checkVersionUtil == null) {
                    checkVersionUtil = new CheckVersionUtils();
                }
            }
        }
        return checkVersionUtil;
    }


    public final String apiToken = "386866f110f4bae7e10f30ee87c8caa7";


    final String ERRO = "-1";
    final String DONOT_UPDATE = "0";
    final String CAN_UPDATE = "1";
    final String HAVETO_UPDATE = "2";
    private PopupVersionSelectView popupVersionSelectView;

    /**
     * 获取服务器版本信息
     */
    public void doVersionCheck(Context mContext, final View parentView, final PopupVersionSelectView.ISelectUpdate iSelectUpdate, boolean todayCheck) {
        if (todayCheck) {//判断是否当天不再提醒
            String nowDate = ToolUtils.timestamp2String(System.currentTimeMillis(), "yyyy-MM-dd");
            String cacheDate = JnCache.getCache(mContext, PopupVersionSelectView.CHECK_VERSION_KEY);
            if (nowDate.equals(cacheDate)) {
                return;
            }
        }
        if (popupVersionSelectView != null) {
            popupVersionSelectView.dismiss();
            popupVersionSelectView = null;
        }
        popupVersionSelectView = new PopupVersionSelectView((Activity) mContext, iSelectUpdate);


        BasePresent.doStaticCommRequest(mContext, new REQ_Factory
                .POST_CHECK_APPVERSION_REQ(""), false, false, new BasePresent.DoCommRequestInterface<BaseResponse, VersionBean>() {
            @Override
            public void doStart() {

            }

            @Override
            public VersionBean doMap(BaseResponse baseResponse) {
                VersionBean bean = VersionBean.fromJSONAuto(baseResponse.orgin, VersionBean.class);
                return bean;
            }

            @Override
            public void onSuccess(VersionBean versionBean) throws Exception {
                if (ToolUtils.isNull(versionBean.version)) {
                    iSelectUpdate.erro();
                    return;
                }
                // 得到当前已安装的app版本信息
                String currentVersionCode = "";
                PackageManager manager = MyApplication.getApp().getPackageManager();
                PackageInfo info = null;
                info = manager.getPackageInfo(MyApplication.getApp().getPackageName(), 0);
                if (info == null) {
                    iSelectUpdate.erro();
                    return;
                } else {
                    currentVersionCode = (info.versionCode + "").trim();
                }
                if (currentVersionCode.equals(versionBean.version)) {
                    iSelectUpdate.doNotUpdate();
                } else {
                    popupVersionSelectView.setContentMsg(versionBean.changelog);
                    popupVersionSelectView.setDownLoadUrl(versionBean.install_url);
                    popupVersionSelectView.showPop(parentView);
                }
            }

            @Override
            public void onError(Throwable e) {
                iSelectUpdate.erro();
            }
        });
    }

    public void readyDownload(final Context mContext) {
        final AlertDialog.Builder wifiBuilder = new AlertDialog.Builder(
                mContext);
        wifiBuilder.setPositiveButton(
                mContext.getResources().getString(
                        R.string.yes),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        downloadApp(mContext);
                    }
                });
//        wifiBuilder.setNegativeButton(
//                MyApplication.getInstance2().getContext().getResources().getString(
//                        R.string.no),
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(
//                            DialogInterface dialog,
//                            int which) {
//                        //直接进入
//                    }
//                });
        wifiBuilder.setMessage(MyApplication.getApp().getResources()
                .getString(R.string.wifitip));

        AlertDialog alertDialog = wifiBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    /**
     * 根据网址用浏览器打开
     */
    public void downloadApp(Context mContext) {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("安装包下载中");
        dialog.setMax(100);
        dialog.show();
        DownloadUtil downloadUtil = DownloadUtil.get();
        downloadUtil.download(popupVersionSelectView.downLoadUrl, "", "apkdownload", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                if (mContext == null || ((Activity) mContext).isDestroyed()) {
                    return;
                }
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        T.showShort(mContext, "下载完成,正打开安装程序");
                        installApk(mContext, file.getPath());
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
                    }
                });
            }

            @Override
            public void onDownloading(int progress) {
                dialog.setProgress(progress);
            }

            @Override
            public void onDownloadFailed() {
                if (mContext == null || ((Activity) mContext).isDestroyed()) {
                    return;
                }
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        T.showShort(mContext, "下载失败，尝试用浏览器打开下载");
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(Uri.parse(popupVersionSelectView.downLoadUrl));
                        mContext.startActivity(intent);
                    }
                });
            }
        });
    }


    private void installApk(Context mContext, String path) {
        File file = new File(path);
        if (file.exists()) {
            Intent installApkIntent = new Intent();
            installApkIntent.setAction(Intent.ACTION_VIEW);
            installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
            installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //适配8.0需要有权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
                if (hasInstallPermission) {
                    //安装应用
                    installApkIntent.setDataAndType(FileProviderUtil.getUri(mContext, file), "application/vnd.android.package-archive");
                    installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (mContext.getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
                        mContext.startActivity(installApkIntent);
                    }
                } else {
                    //跳转至“安装未知应用”权限界面，引导用户开启权限
                    Uri selfPackageUri = Uri.parse("package:" + mContext.getPackageName());
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, selfPackageUri);
                    ((Activity) mContext).startActivityForResult(intent, 2003);
                }
            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    installApkIntent.setDataAndType(FileProviderUtil.getUri(mContext, file), "application/vnd.android.package-archive");
                    installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    installApkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                }
                if (mContext.getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
                    mContext.startActivity(installApkIntent);
                }
            }
        }
    }


    public static class VersionBean extends BaseBean {

        /**
         * name : 聚多多
         * version : 1
         * "changelog":"1、修复部分已发现的Bug。\n2、对接资产模块绑定与解绑银行卡。\n3、对接资产模块提现功能。"
         * updated_at : 1645514805
         * versionShort : 1.0
         * build : 1
         * installUrl : https://download.bq04.com/apps/62148f080d81cc74a9abed73/install?download_token=7ac25ba813f2a9ad2104f3ad92d4d83e&source=update
         * install_url : https://download.bq04.com/apps/62148f080d81cc74a9abed73/install?download_token=7ac25ba813f2a9ad2104f3ad92d4d83e&source=update
         * direct_install_url : https://download.bq04.com/apps/62148f080d81cc74a9abed73/install?download_token=7ac25ba813f2a9ad2104f3ad92d4d83e&source=update
         * update_url : https://d.6short.com/f9s3
         * binary : {"fsize":66738502}
         */

        public String name;
        public String version;
        public String changelog;
        public int updated_at;
        public String versionShort;
        public String build;
        public String installUrl;
        public String install_url;
        public String direct_install_url;
        public String update_url;
        public BinaryBean binary;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getChangelog() {
            return changelog;
        }

        public void setChangelog(String changelog) {
            this.changelog = changelog;
        }

        public int getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(int updated_at) {
            this.updated_at = updated_at;
        }

        public String getVersionShort() {
            return versionShort;
        }

        public void setVersionShort(String versionShort) {
            this.versionShort = versionShort;
        }

        public String getBuild() {
            return build;
        }

        public void setBuild(String build) {
            this.build = build;
        }

        public String getInstallUrl() {
            return installUrl;
        }

        public void setInstallUrl(String installUrl) {
            this.installUrl = installUrl;
        }

        public String getInstall_url() {
            return install_url;
        }

        public void setInstall_url(String install_url) {
            this.install_url = install_url;
        }

        public String getDirect_install_url() {
            return direct_install_url;
        }

        public void setDirect_install_url(String direct_install_url) {
            this.direct_install_url = direct_install_url;
        }

        public String getUpdate_url() {
            return update_url;
        }

        public void setUpdate_url(String update_url) {
            this.update_url = update_url;
        }

        public BinaryBean getBinary() {
            return binary;
        }

        public void setBinary(BinaryBean binary) {
            this.binary = binary;
        }

        public static class BinaryBean extends BaseBean {
            /**
             * fsize : 66738502
             */

            private int fsize;

            public int getFsize() {
                return fsize;
            }

            public void setFsize(int fsize) {
                this.fsize = fsize;
            }
        }
    }
}



