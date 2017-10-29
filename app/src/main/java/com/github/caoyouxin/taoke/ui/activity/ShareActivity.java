package com.github.caoyouxin.taoke.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bilibili.socialize.share.core.shareparam.ShareImage;
import com.bilibili.socialize.share.core.shareparam.ShareParamImage;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.util.ShareHelper;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.handle)
    TextView handle;

    @BindView(R.id.share_text)
    EditText shareText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share);

        ButterKnife.bind(this);

        title.setText(R.string.share_title);
        handle.setText(R.string.share_submit);
    }

    @OnClick({R.id.back, R.id.handle, R.id.share_text_only})
    protected void onBackClick(View view) {
        if (R.id.back == view.getId()) {
            onBackPressed();
            return;
        }


        switch (view.getId()) {
            case R.id.handle:
                String thumbnail = "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg";
                ShareParamImage shareParamImage = new ShareParamImage(getTitle().toString(), "", thumbnail);
                shareParamImage.setImage(new ShareImage(thumbnail));
                ShareHelper.share(this, shareParamImage);

//                DownloadFilesTask downloader = new DownloadFilesTask(this);
//                downloader.execute("http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg");
                break;
            case R.id.share_text_only:
                String text2Share = this.shareText.getText().toString();
                if (text2Share.isEmpty()) {

                    return;
                }

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, text2Share);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
        }
    }

    private static class DownloadFilesTask extends AsyncTask<String, Integer, List<String>> {
        private Context context;

        DownloadFilesTask(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected void onPostExecute(List<String> paths) {
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(this.context.getFileStreamPath(paths.get(0))));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(Intent.createChooser(intent, ((Activity) this.context).getTitle()));
        }

        protected List<String> doInBackground(String... paths) {
            List<String> ret = new ArrayList<>();
            try {
                for (String path : paths) {
                    ret.add(this.download(path));
                }
            } catch (IOException ignored) {
            }
            return ret;
        }

        private String download(String path) throws IOException {
            URL url = new URL(path);
            InputStream is = url.openStream();
            //截取最后的文件名
            String end = path.substring(path.lastIndexOf("."));
            //打开手机对应的输出流,输出到文件中
            String storedPath = "Cache_" + System.currentTimeMillis() + end;
            OutputStream os = this.context.openFileOutput(storedPath, Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int len = 0;
            //从输入六中读取数据,读到缓冲区中
            while((len = is.read(buffer)) > 0)
            {
                os.write(buffer,0,len);
            }
            //关闭输入输出流
            is.close();
            os.close();
            return storedPath;
        }
    }
}
