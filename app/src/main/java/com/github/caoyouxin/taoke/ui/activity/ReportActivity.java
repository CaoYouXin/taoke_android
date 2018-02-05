package com.github.caoyouxin.taoke.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.UploadImageAdapter;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.datasource.UploadImageDataSource;
import com.github.caoyouxin.taoke.model.UploadImageItem;
import com.github.caoyouxin.taoke.model.UserData;
import com.github.caoyouxin.taoke.ui.widget.HackyLoadViewFactory;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yanyusong.y_divideritemdecoration.Y_Divider;
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder;
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;
import com.yanyusong.y_divideritemdecoration.Y_SideLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends BaseActivity {

    private final int OPEN_ALBUM_FLAG = 1023;
    private final int OPEN_CAMERA_FLAG = 1024;
    private final int REQUEST_TAKE_PHOTO_PERMISSION = 1025;
    private final String taoKeTmpPath = "/taoKe_dir/";

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.handle)
    TextView handle;

    @BindView(R.id.report_text)
    EditText reportText;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private GestureDetector gestureDetector = null;
    private MVCHelper<List<UploadImageItem>> recyclerViewMVCHelper = null;
    private UploadImageDataSource uploadImageDataSource = null;
    private String mSaveDir;//拍照存放的文件夹名字
    private String mFileName;//拍照存放的文件的名字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);

        ButterKnife.bind(this);

        title.setText(R.string.feedback);
        handle.setText(R.string.submit);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });

        recyclerView.addItemDecoration(new Y_DividerItemDecoration(this) {
            @Override
            public Y_Divider getDivider(int itemPosition) {
                Y_Divider divider;
                switch (itemPosition % 3) {
                    case 0:
                        divider = new Y_DividerBuilder()
                                .setRightSideLine(true, getResources().getColor(R.color.grey_300), 1, 0, 0)
                                .create();
                        break;
                    case 2:
                        divider = new Y_DividerBuilder()
                                .setLeftSideLine(true, getResources().getColor(R.color.grey_300), 1, 0, 0)
                                .create();
                        break;
                    default:
                        divider = new Y_DividerBuilder().create();
                        break;
                }
                divider.setBottomSideLine(new Y_SideLine(true, getResources().getColor(R.color.green_300), 1, 0, 0));
                return divider;
            }
        });

        UploadImageAdapter uploadImageAdapter = new UploadImageAdapter();

        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                if (!gestureDetector.onTouchEvent(event)) {
                    return false;
                }

                View childView = rv.findChildViewUnder(event.getX(), event.getY());
                int childPosition = rv.getChildAdapterPosition(childView);
                if (0 > childPosition || childPosition >= uploadImageAdapter.getData().size()) {
                    return true;
                }

                UploadImageItem uploadImageItem = uploadImageAdapter.getData().get(childPosition);
                if (!uploadImageItem.isHandle) {
                    return true;
                }

                new AlertDialog.Builder(ReportActivity.this)
                        .setTitle("提示")
                        .setMessage("您可以选择打开[相机]或者[相册]来上传一张图片.")
                        .setPositiveButton("打开相册", (dialog, which) -> {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent, OPEN_ALBUM_FLAG);
                        })
                        .setNegativeButton("打开相机", (dialog, which) -> {
                            if (!checkPermissions()) {
                                //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
                                ActivityCompat.requestPermissions(ReportActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA},
                                        REQUEST_TAKE_PHOTO_PERMISSION);
                            } else {
                                //有权限，直接拍照
                                takePhoto();
                            }
                        })
                        .show();

                return true;
            }
        });

        uploadImageDataSource = new UploadImageDataSource(this);

        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        recyclerViewMVCHelper = new MVCNormalHelper<>(
                recyclerView, hackyLoadViewFactory.madeLoadView(),
                hackyLoadViewFactory.madeLoadMoreView()
        );
        recyclerViewMVCHelper.setAdapter(uploadImageAdapter);
        recyclerViewMVCHelper.setDataSource(uploadImageDataSource);
        recyclerViewMVCHelper.refresh();
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void takePhoto() {
        //指定拍照后的输出路径
        mSaveDir = Environment.getExternalStorageDirectory() + taoKeTmpPath;
        File dir = new File(mSaveDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return;
            }
        }
        //指定拍取照片的名字(以时间戳命名，避免重复)
        mFileName = "WYK" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File file = new File(mSaveDir, mFileName);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果是7.0及以上的系统使用FileProvider的方式创建一个Uri
            photoURI = FileProvider.getUriForFile(ReportActivity.this, "com.hm.camerademo.fileprovider", file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            //7.0以下使用这种方式创建一个Uri
            photoURI = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, OPEN_CAMERA_FLAG);
    }

    private void addNewUploadImageItem(String uri, File file) {
        uploadImageDataSource.addImage(uri);
        recyclerViewMVCHelper.refresh();

        TaoKeApi.uploadImage(file)
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxHandleServerExp(this))
                .subscribe(codeSource -> {
                    uploadImageDataSource.setImageUploaded(uri, codeSource);
                    recyclerViewMVCHelper.refresh();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_TAKE_PHOTO_PERMISSION) {
            if (grantedPermissions(grantResults)) {
                //申请成功，可以拍照
                takePhoto();
            } else {
                Toast.makeText(this, "您需要给[觅券儿]相关授权！", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean grantedPermissions(int[] grantResults) {
        if (grantResults.length <= 0) {
            return false;
        }

        boolean granted = true;
        for (int grantResult : grantResults) {
            granted = grantResult == PackageManager.PERMISSION_GRANTED;

            if (!granted) {
                break;
            }
        }
        return granted;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File file;
        Bitmap bitmap;
        OutputStream outputStream;

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case OPEN_ALBUM_FLAG://从相册获取回调
                Uri originUri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(originUri, proj, null, null, null);
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(columnIndex);
                bitmap = getCompressBitmap(path);

                // mShowIV.setImageBitmap(bitmap);//将选中的图片展示出来

                /* 创建一个新的文件，存放压缩过的bitmap，用于发送给服务器 */
                String saveDir = Environment.getExternalStorageDirectory() + taoKeTmpPath;
                File dir = new File(saveDir);
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        return;
                    }
                }
                //指定拍取照片的名字(以时间戳命名，避免重复)
                String fileName = "WYK" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                file = new File(saveDir, fileName);
                if (file.exists()) {
                    if (!file.delete()) {
                        return;
                    }
                }
                try {
                    if (!file.createNewFile()) {
                        return;
                    }
                    outputStream = new FileOutputStream(file);
                    //将bitmap写入file
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    System.out.println("file size:" + file.length());
                    /* 到这里已经成功将bitmap写入file了，此时可以将file或者流发送给服务器了 */
                    addNewUploadImageItem(Uri.fromFile(file).toString(), file);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case OPEN_CAMERA_FLAG://拍照获取的回调
                file = new File(mSaveDir + mFileName);//拍照前指定的输出路径
                bitmap = getCompressBitmap(mSaveDir + mFileName);

                // mShowIV.setImageBitmap(bitmap);//让拍照的照片显示在控件上

                try {
                    outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    System.out.println("file size:" + file.length());
                    /* 到这里已经成功将bitmap写入file了，此时可以将file或者流发送给服务器了 */
                    addNewUploadImageItem(Uri.fromFile(file).toString(), file);

                    break;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 一般相册或拍照的图片都很大，不会直接传给服务器，所以需要压缩
     *
     * @param path 图片的路径
     * @return 压缩后的图片
     */
    private Bitmap getCompressBitmap(String path) {
        int BASE_SIZE = 400;//需要压缩到的最小宽高
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        //只计算几何尺寸不返回bitmap，这样不会占用内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int width = options.outWidth;
        int height = options.outHeight;
        int min = width < height ? width : height;
        int rate = min / BASE_SIZE;//压缩倍率
        if (rate < 1) rate = 1;
        options.inSampleSize = rate;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    @OnClick({R.id.back, R.id.handle})
    protected void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.handle:
                onSubmitReport();
                break;
        }
    }

    private void onSubmitReport() {
        String reportContent = reportText.getText().toString().trim();
        if (TextUtils.isEmpty(reportContent)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.need_report_content, Snackbar.LENGTH_LONG).show();
            reportText.requestFocus();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (UploadImageItem uploadImageItem : uploadImageDataSource.getData()) {
            if (uploadImageItem.isHandle) {
                continue;
            }
            sb.append('>').append(' ').append(uploadImageItem.code).append('\n');
        }

        TaoKeApi.sendFeedback(String.format("---\ntitle: 来自%s的反馈\n---\n%s\n> 附件\n>\n%s",
                UserData.get().getPhone(), reportContent, sb.toString()))
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxHandleServerExp(this))
                .subscribe(
                        taoKeData -> new AlertDialog.Builder(this)
                                .setPositiveButton(R.string.get_it, (dialog, which) -> onBackPressed())
                                .setMessage(R.string.get_report).show()
                );
    }
}
