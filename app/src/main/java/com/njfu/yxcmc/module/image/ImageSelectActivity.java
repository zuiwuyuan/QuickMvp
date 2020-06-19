package com.njfu.yxcmc.module.image;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.njfu.yxcmc.R;
import com.njfu.yxcmc.base.BaseActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import butterknife.OnClick;

/**
 * 从图库选择图片或直接拍照
 *
 * @author Lolicon
 */
public class ImageSelectActivity extends BaseActivity {

    // 打开系统设置，应用详情
    private static final int ACTIVITY_REQUEST_CODE_TO_SYSTEM_SETTING = 0x00000001;
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000002;

    private static final int REQUEST_CODE_CAMERA = 0x00000011;
    private static final int REQUEST_CODE_OPEN_PHOTO_ALBUM = 0x00000012;
    private static final int REQUEST_CODE_CROP_PHOTO = 0x00000013;

    private boolean isCrop = false;

    //用于保存拍照图片的uri
    private Uri mCameraUri;

    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    // 用于暂存剪裁的图片
    private File mCropImageFile;

    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    private ImageView ivPhoto;

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_select;
    }

    @Override
    public void initView() {
        ivPhoto = findViewById(R.id.ivPhoto);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tvBtnTakePhoto, R.id.tvBtnSelectPhoto, R.id.tvBtnTakeCropPhoto, R.id.tvBtnSelectCropPhoto})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBtnTakePhoto:
                isCrop = false;
                checkCameraPermission();
                break;
            case R.id.tvBtnSelectPhoto:
                isCrop = false;
                photoFromGallery();
                break;
            case R.id.tvBtnTakeCropPhoto:
                isCrop = true;
                checkCameraPermission();
                break;
            case R.id.tvBtnSelectCropPhoto:
                isCrop = true;
                photoFromGallery();
                break;
        }
    }

    private void checkCameraPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CAMERA)
                // 准备方法，和 okhttp 的拦截器一样，在请求权限之前先运行改方法，已经拥有权限不会触发该方法
                .rationale((context, permissions, executor) -> {
                    LogUtils.e("rationale");

                    AlertDialog.Builder builder = new AlertDialog.Builder(ImageSelectActivity.this);
                    builder.setTitle("权限提醒");
                    builder.setMessage("\t\t为了应用能正常拍照，请允许应用开启相机权限");
                    builder.setCancelable(false);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            executor.execute();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            executor.cancel();
                        }
                    });
                    builder.create().show();

                })
                // 用户给权限了
                .onGranted(permissions -> {
                    //有调起相机拍照。
                    openCamera();
                })
                // 用户拒绝权限，包括不再显示权限弹窗也在此列
                .onDenied(permissions -> {

                    // 判断用户是不是不再显示权限弹窗了，若不再显示的话进入权限设置页
                    if (AndPermission.hasAlwaysDeniedPermission(ImageSelectActivity.this, permissions)) {
                        onPermissionForbidden();
                        return;
                    }
                })
                .start();
    }

    private void onPermissionForbidden() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ImageSelectActivity.this);
        builder.setTitle("权限提醒");
        builder.setMessage("\t\t为了应用能正常拍照，请按“确认”按钮后，到设置中心的“权限管理”中开启本应用的相机权限。");
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                // 打开权限设置页
                AndPermission.with(ImageSelectActivity.this)
                        .runtime()
                        .setting()
                        .start(ACTIVITY_REQUEST_CODE_TO_SYSTEM_SETTING);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    /**
     * 处理权限申请的回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                openCamera();
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 调起相机拍照
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // 适配android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }

            mCameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, REQUEST_CODE_CAMERA);
            }
        }
    }

    /**
     * 打开图库，选择照片
     */
    private void photoFromGallery() {
        try {
            Intent intent = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            } else {
                intent = new Intent(Intent.ACTION_PICK);
            }
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_OPEN_PHOTO_ALBUM);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

        }
    }

    private void cropPhoto(String imagePath) {
        mCropImageFile = getmCropImageFile();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    mCameraImagePath = handleImage(mCameraUri);

                    LogUtils.e("mCameraUri ： " + mCameraUri);
                    LogUtils.e("mCameraImagePath ： " + mCameraImagePath);
                    LogUtils.e("isAndroidQ ： " + isAndroidQ);

                    if (isCrop) {
                        cropPhoto(mCameraImagePath);
                    } else {
                        if (isAndroidQ) {
                            ivPhoto.setImageURI(mCameraUri);
                        } else {
                            ivPhoto.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                        }
                    }

                }
                break;

            case REQUEST_CODE_OPEN_PHOTO_ALBUM:
                if (resultCode == RESULT_OK) {
                    mCameraUri = data.getData();
                    mCameraImagePath = handleImage(data.getData());

                    LogUtils.e("mCameraUri ： " + mCameraUri);
                    LogUtils.e("mCameraImagePath ： " + mCameraImagePath);
                    LogUtils.e("isAndroidQ ： " + isAndroidQ);

                    if (isCrop) {
                        cropPhoto(mCameraImagePath);
                    } else {
                        if (isAndroidQ) {
                            ivPhoto.setImageURI(mCameraUri);
                        } else {
                            ivPhoto.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                        }
                    }
                }
                break;
            case REQUEST_CODE_CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    LogUtils.e("mCropImagePath : " + mCropImageFile.getAbsolutePath());
                    ivPhoto.setImageURI(Uri.fromFile(mCropImageFile));
                } else {
                    Toast.makeText(this, "截图失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件(拍照的图片要长期保存)
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    /**
     * 获取裁剪的图片保存地址（裁剪的图片放到缓存中，及时清理）
     */
    private File getmCropImageFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"temp.jpg");
            File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            return file;
        }
        return null;
    }

    /**
     * 把从相册获取到的数据信息，转为图片地址
     *
     * @param uri 从相册获取到的数据
     * @return
     */
    private String handleImage(Uri uri) {

        String imagePath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                            "content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equals(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
        } else {
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 通过uri加载图片
     *
     * @param context
     * @param uri
     * @return
     */
    private Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把fileUri转换成ContentUri
     *
     * @param imageFile
     * @return
     */
    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}