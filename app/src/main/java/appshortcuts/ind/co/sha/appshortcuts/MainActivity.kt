package appshortcuts.ind.co.sha.appshortcuts

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.ShortcutManager
import android.os.Build
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Icon
import android.net.Uri
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var shortcutManager: ShortcutManager? = null

    private val callusShortcutButton: Button by lazy { call_us_shortcut_button }
    private val callusShortcutTextView: TextView by lazy { shortcut_info_text_view }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            initShortCuts()
        } else {
            updateShortcutNotSupportedTextAndHideButton()
        }
    }

    private fun updateShortcutNotSupportedTextAndHideButton() {
        callusShortcutButton.visibility = View.GONE
        callusShortcutTextView.setText(R.string.shortcut_not_supported)
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun initShortCuts() {
        configureShortCutManager()
        if (hasNoShortCuts()) {
            createDynamicShortcuts()
        }
        configureCallUsViews()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun configureShortCutManager() {
        shortcutManager = getSystemService(ShortcutManager::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun configureCallUsViews() {
        when(hasCallusShortCut()) {
            true -> showRemoveShortCutData()
            else -> showAddShortCutData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun showAddShortCutData() {
        callusShortcutButton.setText(R.string.add_call_us_shortcut)
        callusShortcutTextView.setText(R.string.shortcut_was_not_added)
        callusShortcutButton.setOnClickListener {
            createDynamicShortcuts()
            showRemoveShortCutData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun showRemoveShortCutData() {
        callusShortcutButton.setText(R.string.remove_call_us_shortcut)
        callusShortcutTextView.setText(R.string.shortcut_was_added_please_remove_by_clicking_remove_button)
        callusShortcutButton.setOnClickListener {
            removeCallusShortcut()
            showAddShortCutData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun removeCallusShortcut() {
        shortcutManager?.disableShortcuts(listOf(callusShortCutID) )
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun hasNoShortCuts(): Boolean {
        val shortCutsSize = shortcutManager?.dynamicShortcuts?.size
        return shortCutsSize == null || shortCutsSize == 0
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun hasCallusShortCut(): Boolean
        = shortcutManager?.dynamicShortcuts?.any { it.id == callusShortCutID } ?: false

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun createDynamicShortcuts() {
        shortcutManager?.addDynamicShortcuts(getDynamicShortCutList())
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun getDynamicShortCutList() = Arrays.asList(contactUsShortcutInfo(), showProfileShortcutInfo())

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun contactUsShortcutInfo(): ShortcutInfo {
        // dial this number when the shortcut was selected
        val contactUsUri = Uri.parse("tel:1111111")

        return ShortcutInfo.Builder(this, callusShortCutID)
            .setShortLabel(getString(R.string.call_us_shortcut_short_label))
            .setLongLabel(getString(R.string.call_us_shortcut_long_label))
            .setDisabledMessage(getString(R.string.call_us_shortcut_disabled_msg))
            .setIcon(Icon.createWithResource(this, R.drawable.ic_call_black_24dp))
            .setIntent(Intent(Intent.ACTION_DIAL, contactUsUri))
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun showProfileShortcutInfo(): ShortcutInfo {
        val intent = Intent(this, ProfileActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return ShortcutInfo.Builder(this, showProfileShortCutID)
            .setShortLabel(getString(R.string.show_profile_shortcut_short_label))
            .setLongLabel(getString(R.string.show_profile_shortcut_long_label))
            .setIcon(Icon.createWithResource(this, R.drawable.ic_person_black_24dp))
            .setIntent(intent)
            .build()
    }

    private val callusShortCutID = "CALLUS"
    private val showProfileShortCutID = "ShowProfile"
}
