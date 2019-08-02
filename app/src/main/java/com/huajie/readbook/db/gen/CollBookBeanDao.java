package com.huajie.readbook.db.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.huajie.readbook.db.entity.CollBookBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COLL_BOOK_BEAN".
*/
public class CollBookBeanDao extends AbstractDao<CollBookBean, String> {

    public static final String TABLENAME = "COLL_BOOK_BEAN";

    /**
     * Properties of entity CollBookBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, String.class, "_id", true, "_ID");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property ReaderId = new Property(3, String.class, "readerId", false, "READER_ID");
        public final static Property BookId = new Property(4, String.class, "bookId", false, "BOOK_ID");
        public final static Property SectionId = new Property(5, String.class, "sectionId", false, "SECTION_ID");
        public final static Property Author = new Property(6, String.class, "author", false, "AUTHOR");
        public final static Property ShortIntro = new Property(7, String.class, "shortIntro", false, "SHORT_INTRO");
        public final static Property Logo = new Property(8, String.class, "logo", false, "LOGO");
        public final static Property Notes = new Property(9, String.class, "notes", false, "NOTES");
        public final static Property HasCp = new Property(10, boolean.class, "hasCp", false, "HAS_CP");
        public final static Property LatelyFollower = new Property(11, int.class, "latelyFollower", false, "LATELY_FOLLOWER");
        public final static Property RetentionRatio = new Property(12, double.class, "retentionRatio", false, "RETENTION_RATIO");
        public final static Property ClassifyId = new Property(13, String.class, "classifyId", false, "CLASSIFY_ID");
        public final static Property Updated = new Property(14, String.class, "updated", false, "UPDATED");
        public final static Property LastRead = new Property(15, String.class, "lastRead", false, "LAST_READ");
        public final static Property ChaptersCount = new Property(16, int.class, "chaptersCount", false, "CHAPTERS_COUNT");
        public final static Property LastChapter = new Property(17, String.class, "lastChapter", false, "LAST_CHAPTER");
        public final static Property IsUpdate = new Property(18, boolean.class, "isUpdate", false, "IS_UPDATE");
        public final static Property IsLocal = new Property(19, boolean.class, "isLocal", false, "IS_LOCAL");
    }

    private DaoSession daoSession;


    public CollBookBeanDao(DaoConfig config) {
        super(config);
    }
    
    public CollBookBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COLL_BOOK_BEAN\" (" + //
                "\"_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: _id
                "\"TITLE\" TEXT," + // 1: title
                "\"NAME\" TEXT," + // 2: name
                "\"READER_ID\" TEXT," + // 3: readerId
                "\"BOOK_ID\" TEXT," + // 4: bookId
                "\"SECTION_ID\" TEXT," + // 5: sectionId
                "\"AUTHOR\" TEXT," + // 6: author
                "\"SHORT_INTRO\" TEXT," + // 7: shortIntro
                "\"LOGO\" TEXT," + // 8: logo
                "\"NOTES\" TEXT," + // 9: notes
                "\"HAS_CP\" INTEGER NOT NULL ," + // 10: hasCp
                "\"LATELY_FOLLOWER\" INTEGER NOT NULL ," + // 11: latelyFollower
                "\"RETENTION_RATIO\" REAL NOT NULL ," + // 12: retentionRatio
                "\"CLASSIFY_ID\" TEXT," + // 13: classifyId
                "\"UPDATED\" TEXT," + // 14: updated
                "\"LAST_READ\" TEXT," + // 15: lastRead
                "\"CHAPTERS_COUNT\" INTEGER NOT NULL ," + // 16: chaptersCount
                "\"LAST_CHAPTER\" TEXT," + // 17: lastChapter
                "\"IS_UPDATE\" INTEGER NOT NULL ," + // 18: isUpdate
                "\"IS_LOCAL\" INTEGER NOT NULL );"); // 19: isLocal
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COLL_BOOK_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CollBookBean entity) {
        stmt.clearBindings();
 
        String _id = entity.get_id();
        if (_id != null) {
            stmt.bindString(1, _id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String readerId = entity.getReaderId();
        if (readerId != null) {
            stmt.bindString(4, readerId);
        }
 
        String bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindString(5, bookId);
        }
 
        String sectionId = entity.getSectionId();
        if (sectionId != null) {
            stmt.bindString(6, sectionId);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(7, author);
        }
 
        String shortIntro = entity.getShortIntro();
        if (shortIntro != null) {
            stmt.bindString(8, shortIntro);
        }
 
        String logo = entity.getLogo();
        if (logo != null) {
            stmt.bindString(9, logo);
        }
 
        String notes = entity.getNotes();
        if (notes != null) {
            stmt.bindString(10, notes);
        }
        stmt.bindLong(11, entity.getHasCp() ? 1L: 0L);
        stmt.bindLong(12, entity.getLatelyFollower());
        stmt.bindDouble(13, entity.getRetentionRatio());
 
        String classifyId = entity.getClassifyId();
        if (classifyId != null) {
            stmt.bindString(14, classifyId);
        }
 
        String updated = entity.getUpdated();
        if (updated != null) {
            stmt.bindString(15, updated);
        }
 
        String lastRead = entity.getLastRead();
        if (lastRead != null) {
            stmt.bindString(16, lastRead);
        }
        stmt.bindLong(17, entity.getChaptersCount());
 
        String lastChapter = entity.getLastChapter();
        if (lastChapter != null) {
            stmt.bindString(18, lastChapter);
        }
        stmt.bindLong(19, entity.getIsUpdate() ? 1L: 0L);
        stmt.bindLong(20, entity.getIsLocal() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CollBookBean entity) {
        stmt.clearBindings();
 
        String _id = entity.get_id();
        if (_id != null) {
            stmt.bindString(1, _id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String readerId = entity.getReaderId();
        if (readerId != null) {
            stmt.bindString(4, readerId);
        }
 
        String bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindString(5, bookId);
        }
 
        String sectionId = entity.getSectionId();
        if (sectionId != null) {
            stmt.bindString(6, sectionId);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(7, author);
        }
 
        String shortIntro = entity.getShortIntro();
        if (shortIntro != null) {
            stmt.bindString(8, shortIntro);
        }
 
        String logo = entity.getLogo();
        if (logo != null) {
            stmt.bindString(9, logo);
        }
 
        String notes = entity.getNotes();
        if (notes != null) {
            stmt.bindString(10, notes);
        }
        stmt.bindLong(11, entity.getHasCp() ? 1L: 0L);
        stmt.bindLong(12, entity.getLatelyFollower());
        stmt.bindDouble(13, entity.getRetentionRatio());
 
        String classifyId = entity.getClassifyId();
        if (classifyId != null) {
            stmt.bindString(14, classifyId);
        }
 
        String updated = entity.getUpdated();
        if (updated != null) {
            stmt.bindString(15, updated);
        }
 
        String lastRead = entity.getLastRead();
        if (lastRead != null) {
            stmt.bindString(16, lastRead);
        }
        stmt.bindLong(17, entity.getChaptersCount());
 
        String lastChapter = entity.getLastChapter();
        if (lastChapter != null) {
            stmt.bindString(18, lastChapter);
        }
        stmt.bindLong(19, entity.getIsUpdate() ? 1L: 0L);
        stmt.bindLong(20, entity.getIsLocal() ? 1L: 0L);
    }

    @Override
    protected final void attachEntity(CollBookBean entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public CollBookBean readEntity(Cursor cursor, int offset) {
        CollBookBean entity = new CollBookBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // readerId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // bookId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // sectionId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // author
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // shortIntro
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // logo
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // notes
            cursor.getShort(offset + 10) != 0, // hasCp
            cursor.getInt(offset + 11), // latelyFollower
            cursor.getDouble(offset + 12), // retentionRatio
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // classifyId
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // updated
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // lastRead
            cursor.getInt(offset + 16), // chaptersCount
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // lastChapter
            cursor.getShort(offset + 18) != 0, // isUpdate
            cursor.getShort(offset + 19) != 0 // isLocal
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CollBookBean entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setReaderId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBookId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSectionId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAuthor(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setShortIntro(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLogo(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setNotes(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setHasCp(cursor.getShort(offset + 10) != 0);
        entity.setLatelyFollower(cursor.getInt(offset + 11));
        entity.setRetentionRatio(cursor.getDouble(offset + 12));
        entity.setClassifyId(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setUpdated(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setLastRead(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setChaptersCount(cursor.getInt(offset + 16));
        entity.setLastChapter(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setIsUpdate(cursor.getShort(offset + 18) != 0);
        entity.setIsLocal(cursor.getShort(offset + 19) != 0);
     }
    
    @Override
    protected final String updateKeyAfterInsert(CollBookBean entity, long rowId) {
        return entity.get_id();
    }
    
    @Override
    public String getKey(CollBookBean entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CollBookBean entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
