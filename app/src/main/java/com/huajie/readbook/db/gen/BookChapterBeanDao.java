package com.huajie.readbook.db.gen;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.huajie.readbook.db.entity.BookChapterBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BOOK_CHAPTER_BEAN".
*/
public class BookChapterBeanDao extends AbstractDao<BookChapterBean, Void> {

    public static final String TABLENAME = "BOOK_CHAPTER_BEAN";

    /**
     * Properties of entity BookChapterBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Link = new Property(0, String.class, "link", false, "LINK");
        public final static Property Content = new Property(1, String.class, "content", false, "CONTENT");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property IsVip = new Property(3, int.class, "isVip", false, "IS_VIP");
        public final static Property IsPay = new Property(4, int.class, "isPay", false, "IS_PAY");
        public final static Property Id = new Property(5, String.class, "id", false, "ID");
        public final static Property Title = new Property(6, String.class, "title", false, "TITLE");
        public final static Property TaskName = new Property(7, String.class, "taskName", false, "TASK_NAME");
        public final static Property BookId = new Property(8, String.class, "bookId", false, "BOOK_ID");
        public final static Property Unreadble = new Property(9, boolean.class, "unreadble", false, "UNREADBLE");
    }

    private Query<BookChapterBean> collBookBean_BookChapterListQuery;
    private Query<BookChapterBean> downloadTaskBean_BookChapterListQuery;

    public BookChapterBeanDao(DaoConfig config) {
        super(config);
    }
    
    public BookChapterBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOOK_CHAPTER_BEAN\" (" + //
                "\"LINK\" TEXT," + // 0: link
                "\"CONTENT\" TEXT," + // 1: content
                "\"NAME\" TEXT," + // 2: name
                "\"IS_VIP\" INTEGER NOT NULL ," + // 3: isVip
                "\"IS_PAY\" INTEGER NOT NULL ," + // 4: isPay
                "\"ID\" TEXT," + // 5: id
                "\"TITLE\" TEXT," + // 6: title
                "\"TASK_NAME\" TEXT," + // 7: taskName
                "\"BOOK_ID\" TEXT," + // 8: bookId
                "\"UNREADBLE\" INTEGER NOT NULL );"); // 9: unreadble
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_BOOK_CHAPTER_BEAN_TASK_NAME ON BOOK_CHAPTER_BEAN" +
                " (\"TASK_NAME\" ASC);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_BOOK_CHAPTER_BEAN_BOOK_ID ON BOOK_CHAPTER_BEAN" +
                " (\"BOOK_ID\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOOK_CHAPTER_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BookChapterBean entity) {
        stmt.clearBindings();
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(1, link);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(2, content);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
        stmt.bindLong(4, entity.getIsVip());
        stmt.bindLong(5, entity.getIsPay());
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(6, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(7, title);
        }
 
        String taskName = entity.getTaskName();
        if (taskName != null) {
            stmt.bindString(8, taskName);
        }
 
        String bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindString(9, bookId);
        }
        stmt.bindLong(10, entity.getUnreadble() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BookChapterBean entity) {
        stmt.clearBindings();
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(1, link);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(2, content);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
        stmt.bindLong(4, entity.getIsVip());
        stmt.bindLong(5, entity.getIsPay());
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(6, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(7, title);
        }
 
        String taskName = entity.getTaskName();
        if (taskName != null) {
            stmt.bindString(8, taskName);
        }
 
        String bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindString(9, bookId);
        }
        stmt.bindLong(10, entity.getUnreadble() ? 1L: 0L);
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public BookChapterBean readEntity(Cursor cursor, int offset) {
        BookChapterBean entity = new BookChapterBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // link
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // content
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.getInt(offset + 3), // isVip
            cursor.getInt(offset + 4), // isPay
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // id
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // title
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // taskName
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // bookId
            cursor.getShort(offset + 9) != 0 // unreadble
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BookChapterBean entity, int offset) {
        entity.setLink(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setContent(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIsVip(cursor.getInt(offset + 3));
        entity.setIsPay(cursor.getInt(offset + 4));
        entity.setId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTitle(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTaskName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setBookId(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setUnreadble(cursor.getShort(offset + 9) != 0);
     }
    
    @Override
    protected final Void updateKeyAfterInsert(BookChapterBean entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(BookChapterBean entity) {
        return null;
    }

    @Override
    public boolean hasKey(BookChapterBean entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "bookChapterList" to-many relationship of CollBookBean. */
    public List<BookChapterBean> _queryCollBookBean_BookChapterList(String bookId) {
        synchronized (this) {
            if (collBookBean_BookChapterListQuery == null) {
                QueryBuilder<BookChapterBean> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.BookId.eq(null));
                collBookBean_BookChapterListQuery = queryBuilder.build();
            }
        }
        Query<BookChapterBean> query = collBookBean_BookChapterListQuery.forCurrentThread();
        query.setParameter(0, bookId);
        return query.list();
    }

    /** Internal query to resolve the "bookChapterList" to-many relationship of DownloadTaskBean. */
    public List<BookChapterBean> _queryDownloadTaskBean_BookChapterList(String taskName) {
        synchronized (this) {
            if (downloadTaskBean_BookChapterListQuery == null) {
                QueryBuilder<BookChapterBean> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.TaskName.eq(null));
                downloadTaskBean_BookChapterListQuery = queryBuilder.build();
            }
        }
        Query<BookChapterBean> query = downloadTaskBean_BookChapterListQuery.forCurrentThread();
        query.setParameter(0, taskName);
        return query.list();
    }

}
