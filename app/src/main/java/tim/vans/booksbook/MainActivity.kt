package tim.vans.booksbook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject
import tim.vans.booksbook.data.Book
import tim.vans.booksbook.data.BookDatabase


class MainActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(R.string.all_books)
        addTemplateBooks()
        // addWithoutTemplateBooks()
    }

    private fun addWithoutTemplateBooks() {
        val bookDB = BookDatabase.getDatabase(this)
        val bookDao = bookDB.bookDao()
        val books = bookDao.getAllData()
        bookDB.clearAllTables()

        val recyclerView :RecyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val myAdapter = Adapter(books)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        recyclerView.adapter = myAdapter

        buttonCheck(books)

    }

    private fun buttonCheck(books: List<Book>){
        val button = findViewById<View>(R.id.findBooksButton)

        if (books.isEmpty()){
            button.setOnClickListener {
                // Source: https://www.javatpoint.com/android-intent-tutorial#:~:text=Android%20Intent%20is%20the%20message,intent%20is%20intention%20or%20purpose.
                val url = "https://www.goodreads.com/book"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        } else {
            button.visibility = View.INVISIBLE
        }
    }

    private fun addTemplateBooks(){
        val bookDB = BookDatabase.getDatabase(this)
        val bookDao = bookDB.bookDao()
        bookDB.clearAllTables()

        val templateBook1 = Book(0,"The Hobbit", "J.R.R Tolkien", 4,"worth the read!")
        val templateBook2 = Book(0,"The Fellowship of the Ring", "J.R.R Tolkien",  3,"expected more")
        val templateBook3 = Book(0,"The Two Towers", "J.R.R Tolkien", 5,"best book of all time")

        bookDao.addAllBooks(templateBook1,templateBook2,templateBook3)
        val books = bookDao.getAllData()

        val recyclerView :RecyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val myAdapter = Adapter(books)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        recyclerView.adapter = myAdapter

        buttonCheck(books)
    }


    // Source for UNUSED_PARAMETER: https://stackoverflow.com/a/49872468
    fun showAddBook(@Suppress("UNUSED_PARAMETER")view: View){
        setContentView(R.layout.activity_scan)
        setTitle(R.string.scanning_book)
        val scanButton:Button = findViewById(R.id.scanButton)
        scanButton.setOnClickListener(this)
    }

    fun goHome(@Suppress("UNUSED_PARAMETER")view: View){
        setTitle(R.string.all_books)
        setContentView(R.layout.activity_main)
        addWithoutTemplateBooks()
    }

    // Source for Barcode Scanner: https://github.com/journeyapps/zxing-android-embedded

    private fun scanCode(){
       val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES)
        val message = getString(R.string.scan_barcode)
        integrator.setPrompt(message)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(false)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, R.string.cancelled , Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.scanned , Toast.LENGTH_SHORT).show()
                fetchBookData(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onClick(view: View?) {
        scanCode()
    }

    private fun fetchBookData(contents: String) {
        // Instantiate the RequestQueue
        // Source: https://developer.android.com/training/volley/simple

        val queue = Volley.newRequestQueue(this)
        val url = "https://www.googleapis.com/books/v1/volumes?q=isbn%3D${contents}"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    val totalItems = JSONObject(response).getInt("totalItems")
                    if (totalItems > 0) {
                        val fetchedBookJSON = JSONObject(response).getJSONArray("items")[0]
                        bookCheck(fetchedBookJSON as JSONObject, contents)
                    } else {
                        val isbnQuestionDialog = isbnQuestion(contents,R.string.ISBN_question1)
                        isbnQuestionDialog.show()
                    }

                },
                { print("fetch failed") })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun bookCheck(fetchedBookJSON: JSONObject, ISBN:String) {
        val bookInfo = fetchedBookJSON.getJSONObject("volumeInfo")
        val title = bookInfo.getString("title")
        val authors = bookInfo.getJSONArray("authors")
        val author: String = authors[0] as String

        val bookQuestionDialog = bookQuestion(author,title, ISBN)
        bookQuestionDialog.show()
    }

    // source: https://www.journaldev.com/309/android-alert-dialog-using-kotlin
    private fun bookQuestion(author:String, title: String, ISBN: String): AlertDialog.Builder {
        val bookQuestionDialog = AlertDialog.Builder(this)
        bookQuestionDialog.setTitle(R.string.book_question)
        bookQuestionDialog.setMessage(author.plus(": ").plus(title))
        bookQuestionDialog.setPositiveButton(R.string.yes) { _, _ ->
            goToManualAdd()
            val inputTitle: TextInputLayout = findViewById(R.id.textInputTitle)
            inputTitle.editText?.setText(title)
            val inputAuthor: TextInputLayout = findViewById(R.id.textInputAuthor)
            inputAuthor.editText?.setText(author)
        }
        bookQuestionDialog.setNegativeButton(R.string.no) { _, _ ->
            val isbnQuestionDialog = isbnQuestion(ISBN,R.string.ISBN_question2)
            isbnQuestionDialog.show()
        }
        return bookQuestionDialog
    }

    private fun isbnQuestion(ISBN: String, message: Int): AlertDialog.Builder {
        val isbnQuestionDialog = AlertDialog.Builder(this)

        isbnQuestionDialog.setTitle(message)
        isbnQuestionDialog.setMessage(ISBN)
        isbnQuestionDialog.setPositiveButton(R.string.yes) { _, _ ->
            goToManualAdd()
        }
        isbnQuestionDialog.setNegativeButton(R.string.no) { _, _ ->
            val againQuestionDialog = tryAgainQuestion()
            againQuestionDialog.show()
        }
        return isbnQuestionDialog
    }

    private fun tryAgainQuestion(): AlertDialog.Builder {
        val againQuestionDialog = AlertDialog.Builder(this)

        againQuestionDialog.setTitle(R.string.again_question)
        againQuestionDialog.setPositiveButton(R.string.again) { _, _ ->
            scanCode()

        }
        againQuestionDialog.setNegativeButton(R.string.manual_add) { _, _ ->
            goToManualAdd()
        }
        return againQuestionDialog
    }

    private fun checkInputs(title: String,author: String,opinion:String,score:String){
        val submitButton = findViewById<View>(R.id.submitButton)
        if (title.isEmpty() or author.isEmpty() or opinion.isEmpty() or score.isEmpty()){
            submitButton.isEnabled = false
        } else submitButton.isEnabled = !((score.toInt() < 0) or (score.toInt() > 5))
    }

    private fun goToManualAdd(){
        setContentView(R.layout.manual_add)
        setTitle(R.string.manual_add)

        val inputTitle: TextInputLayout = findViewById(R.id.textInputTitle)
        val title = inputTitle.editText
        val inputAuthor: TextInputLayout = findViewById(R.id.textInputAuthor)
        val author = inputAuthor.editText
        val inputOpinion: TextInputLayout = findViewById(R.id.textInputOpinion)
        val opinion = inputOpinion.editText
        val inputScore: TextInputLayout = findViewById(R.id.textInputScore)
        val score = inputScore.editText

        // Source: https://www.tutorialspoint.com/how-to-use-the-textwatcher-class-in-kotlin

        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputs(title?.text.toString(),author?.text.toString(),opinion?.text.toString(),score?.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        title?.addTextChangedListener(textWatcher)
        author?.addTextChangedListener(textWatcher)
        opinion?.addTextChangedListener(textWatcher)
        score?.addTextChangedListener(textWatcher)
    }

    fun goToManualButton(@Suppress("UNUSED_PARAMETER")view: View){
        goToManualAdd()
    }


    fun manualAdd(@Suppress("UNUSED_PARAMETER")view: View){
        val inputTitle: TextInputLayout = findViewById(R.id.textInputTitle)
        val title = inputTitle.editText?.text.toString()
        val inputAuthor: TextInputLayout = findViewById(R.id.textInputAuthor)
        val author = inputAuthor.editText?.text.toString()
        val inputOpinion: TextInputLayout = findViewById(R.id.textInputOpinion)
        val opinion = inputOpinion.editText?.text.toString()
        val inputScore: TextInputLayout = findViewById(R.id.textInputScore)
        val score = inputScore.editText?.text.toString()
        val enteredBook = Book(0,title,author,score.toInt(),opinion)

        val bookDB = BookDatabase.getDatabase(this)
        val bookDao = bookDB.bookDao()
        bookDao.addBook(enteredBook)
        val books = bookDao.getAllData()
        setContentView(R.layout.activity_main)
        val button = findViewById<View>(R.id.findBooksButton)
        button.visibility = View.INVISIBLE
        val recyclerView :RecyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val myAdapter = Adapter(books)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        recyclerView.adapter = myAdapter

    }

}
