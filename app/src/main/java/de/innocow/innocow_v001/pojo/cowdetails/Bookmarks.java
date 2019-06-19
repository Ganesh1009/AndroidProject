package de.innocow.innocow_v001.pojo.cowdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.innocow.innocow_v001.pojo.cowsearch.BookmarksList;

public class Bookmarks {
    @SerializedName("value")
    @Expose
    private List<BookmarksList> bookmarksList;

    public List<BookmarksList> getBookmarksList(){
        return bookmarksList;
    }

    public void setBookmarksList(List<BookmarksList> bookmarksList) {
        this.bookmarksList = bookmarksList;
    }

    @Override
    public String toString() {
        return "Bookmarks{" +
                "bookmarksList=" + bookmarksList +
                '}';
    }

}

