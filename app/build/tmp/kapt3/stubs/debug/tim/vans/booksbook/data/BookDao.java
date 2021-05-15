package tim.vans.booksbook.data;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\bg\u0018\u00002\u00020\u0001J!\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005\"\u00020\u0006H\'\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0006H\'J\u000e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\u000bH\'\u00a8\u0006\f"}, d2 = {"Ltim/vans/booksbook/data/BookDao;", "", "addAllBooks", "", "books", "", "Ltim/vans/booksbook/data/Book;", "([Ltim/vans/booksbook/data/Book;)V", "addBook", "book", "getAllData", "", "app_debug"})
public abstract interface BookDao {
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.ABORT)
    public abstract void addBook(@org.jetbrains.annotations.NotNull()
    tim.vans.booksbook.data.Book book);
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    public abstract void addAllBooks(@org.jetbrains.annotations.NotNull()
    tim.vans.booksbook.data.Book... books);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * from book_table ORDER BY id ASC")
    public abstract java.util.List<tim.vans.booksbook.data.Book> getAllData();
}