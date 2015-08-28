package com.lewis.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 内容提供器
 * Created by Administrator on 15-8-27.
 */
public class DatabaseProvider extends ContentProvider{

    public final static int BOOK_DIR = 0;
    public final static int BOOK_ITEM = 1;
    public final static int CATEGORY_DIR = 2;
    public final static int CATEGORY_ITEM = 3;
    public final static String AUTHORITY = "com.example.databasetest.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#",CATEGORY_ITEM);
    }

    /**
     * 初始化内容提供器的时候调用，通常会在这里完成对数据库的创建和升级操作,注意只有当存在ContentResolver尝试
     * 访问我们程序中的数据时，内容提供器才会被初始化
     * @return 返回true表示内容提供器初始化成功,返回false则表示初始化失败
     */
    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(),"BookStore.db",null,1);
        return true;
    }

    /**
     * 查询数据
     * @param uri 确定查询哪张表
     * @param projection 确定查询哪些列
     * @param selection 约束查询哪些行的条件
     * @param selectionArgs 约束查询哪些行的条件的参数
     * @param sortOrder 对结果进行排序
     * @return 返回最后的Cursor对象
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection
            , String[] selectionArgs, String sortOrder) {
        //查询数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor = db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book",projection,"id = ?",new String[]{bookId},null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category",projection,"id = ?",new String[]{categoryId},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    /**
     * 根据传入的内容来返回相应的MIME类型
     * @param uri
     * @return
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd."+AUTHORITY+".book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd."+AUTHORITY+".book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd."+AUTHORITY+".category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd."+AUTHORITY+".category";
        }
        return null;
    }

    /**
     * 项内容中添加一条数据
     * @param uri 确定要添加的表
     * @param values 带添加的数据的值
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //添加数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case BOOK_ITEM:
            case BOOK_DIR:
                long newBookId = db.insert("Book",null,values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
                break;
            case CATEGORY_ITEM:
            case CATEGORY_DIR:
                long newCategoryId = db.insert("Category",null,values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/category/"+newCategoryId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    /**
     * 从内容提供器中删除数据
     * @param uri 确定删除哪一张表
     * @param selection 约束删除哪些行
     * @param selectionArgs 约束删除哪些行
     * @return 被删除的行数
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //删除数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRows = db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Book","id = ?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows = db.delete("Category",selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Category","id = ?",new String[]{categoryId});
                break;
        }
        return deleteRows;
    }

    /**
     * 更新内容提供器中的已有数据
     * @param uri 确定更新哪一张表
     * @param values 新数据
     * @param selection 约束更新哪些行
     * @param selectionArgs 约束更新哪些行
     * @return 受影响的行数
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows = db.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update("Book",values,"id = ?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update("Category",values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update("Category",values,"id = ?",new String[]{categoryId});
                break;
        }
        return updateRows;
    }
}
