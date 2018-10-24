package com.example.ginggingi.getfiles.Utils

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import java.io.Serializable

class FileRake: AsyncTask<Void, Void, Boolean> {

    private var callback: Callback
    private var Fileuri: Uri
    private var Fileprojection: Array<String>
    private var contentResolver: ContentResolver

    lateinit var cursor: Cursor
    var columnIndex: Int = 0
    var columnDisplayname: Int = 0

    var result = ArrayList<String>()

    constructor(contentResolver: ContentResolver, Fileuri: Uri, Fileprojection: Array<String>, callback: Callback){
        this.contentResolver = contentResolver
        this.Fileuri = Fileuri
        this.Fileprojection = Fileprojection
        this.callback = callback
    }


    override fun onPreExecute() {
        super.onPreExecute()

        cursor = contentResolver.query(
                Fileuri, //Content://로 시작하는 uri Videouri로하였으면 Video만 가져옴.
                Fileprojection, //어떤 컬럼을 출력할 것인지 ))Data, Data_Name
                null,//어떤 row 를 출력할 것인지
                null,
                MediaStore.MediaColumns.DATE_ADDED + " desc")//어떻게 정리할것인지 ))오래된날자 역순으로(최신순으로).

        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)

    }

    override fun doInBackground(vararg params: Void?): Boolean {
        result = ArrayList<String>()
//        해당 uri로 갯수와 데이터경로를 가져옴

        var lastIndex: Int

        while (cursor.moveToNext()){
            val absolutePathOfVideo = cursor.getString(columnIndex)
            val nameOfFile = cursor.getString(columnDisplayname)
            lastIndex = absolutePathOfVideo.lastIndexOf(nameOfFile)
            lastIndex = if(lastIndex >= 0) lastIndex else (nameOfFile.length - 1)

            if (!TextUtils.isEmpty(absolutePathOfVideo))
                result.add(absolutePathOfVideo)
        }
        cursor.close()

        for (i in result){
            Log.i("GetAbsolutePath ->", "|" + i + "|")
        }
        return result.size > 0
    }

    override fun onPostExecute(isresultValid: Boolean?) {
        super.onPostExecute(isresultValid)
        if (isresultValid!!)
            callback.SuccessCallback(result)
        else
            callback.FailedCallback()

    }
}

interface Callback: Serializable {
    fun SuccessCallback(uris: ArrayList<String>)
    fun FailedCallback()
}

