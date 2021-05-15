package tim.vans.booksbook.data

import androidx.room.*

// Source for all Android Room code: https://www.youtube.com/watch?v=lwAvI3WDXBY&t=809s & https://developer.android.com/training/data-storage/room#additional-resources

@Dao
interface BookDao {

    // if book already exists don't add it again
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addBook(book: Book)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllBooks(vararg books: Book)

    @Query("SELECT * from book_table ORDER BY id ASC")
    fun getAllData(): List<Book>

}