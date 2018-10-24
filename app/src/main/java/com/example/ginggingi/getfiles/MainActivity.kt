package com.example.ginggingi.getfiles

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import com.example.ginggingi.getfiles.Utils.Callback
import com.example.ginggingi.getfiles.Utils.FileRake
import com.example.ginggingi.getfiles.Utils.PermissionChker
import com.example.ginggingi.getfiles.models.MediaModels

import com.example.ginggingi.getfiles.models.PermissionModels
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var RPHandler: PermissionChker
    private lateinit var FRake: FileRake

    private lateinit var pModels: PermissionModels
    private lateinit var mModels: MediaModels

    private lateinit var PList: Array<String>
    private lateinit var FList: ArrayList<String>

    private lateinit var uri: Uri
    private lateinit var projection: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionInit()
        FRakeInit()

        RPHandler.RequestPermission(
                this,
                PList,
                1,
                object: PermissionChker.RequestPermissionListener{
                    override fun onSuccess() {
                        Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_LONG).show()
                        FileRake(contentResolver, uri, projection, object : Callback {
                            override fun SuccessCallback(uris: ArrayList<String>) {
                                FList = uris
                                Log.i("getFiles", "Success")
                                setTextView()
                            }
                            override fun FailedCallback() {
                                Log.i("getFiles","Cannot found Data")
                            }
                        }).execute()
                    }
                    override fun onFailed() {
                        Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
        )
    }

    private fun PermissionInit() {
        pModels = PermissionModels()
        PList = arrayOf(
                pModels.readExStorage,
                pModels.writeExStorage
        )
        RPHandler = PermissionChker()
    }

    private fun FRakeInit() {
        mModels = MediaModels()
        uri = mModels.VideoUri
        projection = arrayOf(
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME
        )


    }

    private fun setTextView(){
        var Str = ""
        for (i in FList){
            Str += i+"\n"
        }
        Txv.setText(Str)
    }
    @Override
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        RPHandler.RequestPermissionsResult(requestCode, PList, grantResults)
    }
}
