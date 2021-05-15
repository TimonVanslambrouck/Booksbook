package tim.vans.booksbook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext

// Source for all Android Room code: https://www.youtube.com/watch?v=lwAvI3WDXBY&t=809s & https://developer.android.com/training/data-storage/room#additional-resources

@Database(entities = [Book::class], version = 2, exportSchema = false )
abstract class BookDatabase: RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object{
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                // instance already exists
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    BookDatabase::class.java,
                    "book_database"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}