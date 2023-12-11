package id.bpdlh.fdb.core.data.sources.local

import android.content.Context
import android.content.SharedPreferences
import id.bpdlh.fdb.core.BuildConfig

class LocalPreferences(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(BuildConfig.PREFS_NAME, Context.MODE_PRIVATE)

    fun save(keyName: String, text: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(keyName, text)
        editor.apply()
    }

    fun save(keyName: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(keyName, value)
        editor.apply()
    }

    fun save(keyName: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(keyName, status)
        editor.apply()
    }

    fun save(keyName: String, value: List<String>) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putStringSet(keyName, value.toSet())
        editor.apply()
    }

    fun getValueList(keyName: String, defaultValue: List<String> = listOf()): List<String> {
        return sharedPref.getStringSet(keyName, defaultValue.toSet())?.toList() ?: listOf()
    }

    fun getValueString(keyName: String): String? {
        return sharedPref.getString(keyName, null)
    }

    fun getValueInt(keyName: String): Int {
        return sharedPref.getInt(keyName, 0)
    }

    fun getValueBoolean(keyName: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(keyName, defaultValue)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor.clear()
        editor.apply()
    }

    fun removeValue(keyName: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(keyName)
        editor.apply()
    }
}