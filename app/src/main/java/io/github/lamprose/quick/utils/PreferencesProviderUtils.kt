package io.github.lamprose.quick.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri


object PreferencesProviderUtils {

    fun putString(
        context: Context,
        spName: String,
        key: String?,
        value: String?
    ): Boolean {
        val uri: Uri? = buildUri(
            PreferencesProvider.STRING_CONTENT_URI_CODE,
            spName,
            key,
            value
        )
        try {
            uri?.let {
                val values = ContentValues()
                values.put(key, value)
                context.contentResolver.insert(it, values)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun remove(context: Context, spName: String, key: String?): Boolean {
        try {
            val uri: Uri? = buildUri(
                PreferencesProvider.DELETE_CONTENT_URI_CODE,
                spName,
                key,
                null
            )
            uri?.let {
                val cr: ContentResolver = context.contentResolver
                cr.delete(uri, null, null)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getString(context: Context, spName: String, key: String?): String {
        return getString(context, spName, key, "")
    }

    fun getString(
        context: Context,
        spName: String,
        key: String?,
        defaultValue: String
    ): String {
        var result = defaultValue
        val uri: Uri? = buildUri(
            PreferencesProvider.STRING_CONTENT_URI_CODE,
            spName,
            key,
            defaultValue
        )
        uri?.let {
            val cursor = context.contentResolver.query(it, null, null, null, null) ?: return result
            if (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(PreferencesProvider.COLUMNNAME))
            }
            return result
        }
        return result
    }

    fun putInt(
        context: Context,
        spName: String,
        key: String?,
        value: Int
    ): Boolean {
        val uri: Uri? = buildUri(
            PreferencesProvider.INTEGER_CONTENT_URI_CODE,
            spName,
            key,
            value
        )
        try {
            uri?.let {
                val values = ContentValues()
                values.put(key, value)
                context.contentResolver.insert(it, values)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getInt(context: Context, spName: String, key: String?): Int {
        return getInt(context, spName, key, -1)
    }

    fun getInt(
        context: Context,
        spName: String,
        key: String?,
        defaultValue: Int
    ): Int {
        var result = defaultValue
        val uri: Uri? = buildUri(
            PreferencesProvider.INTEGER_CONTENT_URI_CODE,
            spName,
            key,
            defaultValue
        )
        uri?.let {
            val cursor = context.contentResolver.query(it, null, null, null, null) ?: return result
            if (cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex(PreferencesProvider.COLUMNNAME))
            }
        }
        return result
    }

    fun putLong(
        context: Context,
        spName: String,
        key: String?,
        value: Long
    ): Boolean {
        val uri: Uri? = buildUri(
            PreferencesProvider.LONG_CONTENT_URI_CODE,
            spName,
            key,
            value
        )
        try {
            uri?.let {
                val values = ContentValues()
                values.put(key, value)
                context.contentResolver.insert(it, values)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getLong(context: Context, spName: String, key: String?): Long {
        return getLong(context, spName, key, -1)
    }

    fun getLong(
        context: Context,
        spName: String,
        key: String?,
        defaultValue: Long
    ): Long {
        var result = defaultValue
        val uri: Uri? = buildUri(
            PreferencesProvider.LONG_CONTENT_URI_CODE,
            spName,
            key,
            defaultValue
        )
        uri?.let {
            val cursor = context.contentResolver.query(uri, null, null, null, null) ?: return result
            if (cursor.moveToNext()) {
                result = cursor.getLong(cursor.getColumnIndex(PreferencesProvider.COLUMNNAME))
            }
        }
        return result
    }

    fun putFloat(
        context: Context,
        spName: String,
        key: String?,
        value: Float
    ): Boolean {
        val uri: Uri? = buildUri(
            PreferencesProvider.FLOAT_CONTENT_URI_CODE,
            spName,
            key,
            value
        )
        try {
            uri?.let {
                val values = ContentValues()
                values.put(key, value)
                context.contentResolver.insert(it, values)
                return true
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getFloat(context: Context, spName: String, key: String?): Float {
        return getFloat(context, spName, key, -1f)
    }

    fun getFloat(
        context: Context,
        spName: String,
        key: String?,
        defaultValue: Float
    ): Float {
        var result = defaultValue
        val uri: Uri? = buildUri(
            PreferencesProvider.FLOAT_CONTENT_URI_CODE,
            spName,
            key,
            defaultValue
        )
        uri?.let {
            val cursor = context.contentResolver.query(uri, null, null, null, null) ?: return result
            if (cursor.moveToNext()) {
                result = cursor.getFloat(cursor.getColumnIndex(PreferencesProvider.COLUMNNAME))
            }
        }
        return result
    }

    fun putBoolean(
        context: Context,
        spName: String,
        key: String?,
        value: Boolean
    ): Boolean {
        val uri: Uri? = buildUri(
            PreferencesProvider.BOOLEAN_CONTENT_URI_CODE,
            spName,
            key,
            value
        )
        try {
            uri?.let {
                val values = ContentValues()
                values.put(key, value)
                context.contentResolver.insert(uri, values)
                return true
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getBoolean(context: Context, spName: String, key: String?): Boolean {
        return getBoolean(context, spName, key, false)
    }

    fun getBoolean(
        context: Context,
        spName: String,
        key: String?,
        defaultValue: Boolean
    ): Boolean {
        var result = defaultValue
        val uri: Uri? = buildUri(
            PreferencesProvider.BOOLEAN_CONTENT_URI_CODE,
            spName,
            key,
            defaultValue
        )
        uri?.let {
            val cursor = context.contentResolver.query(it, null, null, null, null) ?: return result
            if (cursor.moveToNext()) {
                result = java.lang.Boolean.valueOf(
                    cursor.getString(
                        cursor.getColumnIndex(PreferencesProvider.COLUMNNAME)
                    )
                )
            }
        }
        return result
    }

    fun put(context: Context, spName: String, datas: ContentValues?): Boolean {
        val uri: Uri? = buildUri(
            PreferencesProvider.PUTS_CONTENT_URI_CODE,
            spName,
            null,
            null
        )
        try {
            uri?.let {
                context.contentResolver.insert(uri, datas)
                return true
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun buildUri(
        code: Int,
        spName: String,
        key: String?,
        value: Any?
    ): Uri? {
        val authorities = "io.github.lamprose.quick.utils.PreferencesProvider"
        var uri: Uri? = null
        when (code) {
            PreferencesProvider.STRING_CONTENT_URI_CODE -> uri = Uri
                .parse("content://$authorities/string/$spName/$key/$value")
            PreferencesProvider.INTEGER_CONTENT_URI_CODE -> uri = Uri
                .parse("content://$authorities/integer/$spName/$key/$value")
            PreferencesProvider.LONG_CONTENT_URI_CODE -> uri = Uri
                .parse("content://$authorities/long/$spName/$key/$value")
            PreferencesProvider.FLOAT_CONTENT_URI_CODE -> uri = Uri
                .parse("content://$authorities/float/$spName/$key/$value")
            PreferencesProvider.BOOLEAN_CONTENT_URI_CODE -> uri = Uri
                .parse("content://$authorities/boolean/$spName/$key/$value")
            else -> {
            }
        }
        return uri
    }
}