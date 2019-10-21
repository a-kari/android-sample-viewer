package jp.neechan.sampleviewer

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import jp.neechan.sampleviewer.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PDF_PATH = "/storage/emulated/0/Sample/sample.pdf"
        private const val DOC_PATH = "/storage/emulated/0/Sample/sample.doc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (PermissionUtils.checkPermissions(this)) {
            enableButtons()
        }
        setupListeners()
    }

    private fun setupListeners() {
        pdfButton.setOnClickListener { openDocument(PDF_PATH) }
        docButton.setOnClickListener { openDocument(DOC_PATH) }
    }

    private fun enableButtons() {
        pdfButton.isEnabled = true
        docButton.isEnabled = true
    }

    private fun openDocument(path: String) {
        val mimeType    = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path))
        val document    = File(path)
        val documentUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", document)

        val openDocumentIntent = Intent(Intent.ACTION_VIEW)
        openDocumentIntent.setDataAndType(documentUri, mimeType)
        openDocumentIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(openDocumentIntent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableButtons()
        }
    }
}
