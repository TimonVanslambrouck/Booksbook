// Generated by view binder compiler. Do not edit!
package tim.vans.booksbook.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import tim.vans.booksbook.R;

public final class CardItemBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final TextView bookAuthor;

  @NonNull
  public final ImageView bookCover;

  @NonNull
  public final TextView bookOpinion;

  @NonNull
  public final TextView bookScore;

  @NonNull
  public final TextView bookTitle;

  @NonNull
  public final CardView cardView;

  /**
   * This binding is not available in all configurations.
   * <p>
   * Present:
   * <ul>
   *   <li>layout-land/</li>
   * </ul>
   *
   * Absent:
   * <ul>
   *   <li>layout/</li>
   * </ul>
   */
  @Nullable
  public final Guideline guideline5;

  private CardItemBinding(@NonNull CardView rootView, @NonNull TextView bookAuthor,
      @NonNull ImageView bookCover, @NonNull TextView bookOpinion, @NonNull TextView bookScore,
      @NonNull TextView bookTitle, @NonNull CardView cardView, @Nullable Guideline guideline5) {
    this.rootView = rootView;
    this.bookAuthor = bookAuthor;
    this.bookCover = bookCover;
    this.bookOpinion = bookOpinion;
    this.bookScore = bookScore;
    this.bookTitle = bookTitle;
    this.cardView = cardView;
    this.guideline5 = guideline5;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static CardItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CardItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.card_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CardItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bookAuthor;
      TextView bookAuthor = rootView.findViewById(id);
      if (bookAuthor == null) {
        break missingId;
      }

      id = R.id.bookCover;
      ImageView bookCover = rootView.findViewById(id);
      if (bookCover == null) {
        break missingId;
      }

      id = R.id.bookOpinion;
      TextView bookOpinion = rootView.findViewById(id);
      if (bookOpinion == null) {
        break missingId;
      }

      id = R.id.bookScore;
      TextView bookScore = rootView.findViewById(id);
      if (bookScore == null) {
        break missingId;
      }

      id = R.id.bookTitle;
      TextView bookTitle = rootView.findViewById(id);
      if (bookTitle == null) {
        break missingId;
      }

      CardView cardView = (CardView) rootView;

      id = R.id.guideline5;
      Guideline guideline5 = rootView.findViewById(id);

      return new CardItemBinding((CardView) rootView, bookAuthor, bookCover, bookOpinion, bookScore,
          bookTitle, cardView, guideline5);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
